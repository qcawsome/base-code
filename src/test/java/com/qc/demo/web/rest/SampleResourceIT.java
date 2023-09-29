package com.qc.demo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.qc.demo.IntegrationTest;
import com.qc.demo.domain.Sample;
import com.qc.demo.repository.SampleRepository;
import com.qc.demo.service.criteria.SampleCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SampleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SampleResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/samples";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSampleMockMvc;

    private Sample sample;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sample createEntity(EntityManager em) {
        Sample sample = new Sample().text(DEFAULT_TEXT);
        return sample;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sample createUpdatedEntity(EntityManager em) {
        Sample sample = new Sample().text(UPDATED_TEXT);
        return sample;
    }

    @BeforeEach
    public void initTest() {
        sample = createEntity(em);
    }

    @Test
    @Transactional
    void createSample() throws Exception {
        int databaseSizeBeforeCreate = sampleRepository.findAll().size();
        // Create the Sample
        restSampleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sample)))
            .andExpect(status().isCreated());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeCreate + 1);
        Sample testSample = sampleList.get(sampleList.size() - 1);
        assertThat(testSample.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    void createSampleWithExistingId() throws Exception {
        // Create the Sample with an existing ID
        sample.setId(1L);

        int databaseSizeBeforeCreate = sampleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSampleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sample)))
            .andExpect(status().isBadRequest());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSamples() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList
        restSampleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sample.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)));
    }

    @Test
    @Transactional
    void getSample() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get the sample
        restSampleMockMvc
            .perform(get(ENTITY_API_URL_ID, sample.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sample.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT));
    }

    @Test
    @Transactional
    void getSamplesByIdFiltering() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        Long id = sample.getId();

        defaultSampleShouldBeFound("id.equals=" + id);
        defaultSampleShouldNotBeFound("id.notEquals=" + id);

        defaultSampleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSampleShouldNotBeFound("id.greaterThan=" + id);

        defaultSampleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSampleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSamplesByTextIsEqualToSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where text equals to DEFAULT_TEXT
        defaultSampleShouldBeFound("text.equals=" + DEFAULT_TEXT);

        // Get all the sampleList where text equals to UPDATED_TEXT
        defaultSampleShouldNotBeFound("text.equals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    void getAllSamplesByTextIsInShouldWork() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where text in DEFAULT_TEXT or UPDATED_TEXT
        defaultSampleShouldBeFound("text.in=" + DEFAULT_TEXT + "," + UPDATED_TEXT);

        // Get all the sampleList where text equals to UPDATED_TEXT
        defaultSampleShouldNotBeFound("text.in=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    void getAllSamplesByTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where text is not null
        defaultSampleShouldBeFound("text.specified=true");

        // Get all the sampleList where text is null
        defaultSampleShouldNotBeFound("text.specified=false");
    }

    @Test
    @Transactional
    void getAllSamplesByTextContainsSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where text contains DEFAULT_TEXT
        defaultSampleShouldBeFound("text.contains=" + DEFAULT_TEXT);

        // Get all the sampleList where text contains UPDATED_TEXT
        defaultSampleShouldNotBeFound("text.contains=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    void getAllSamplesByTextNotContainsSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where text does not contain DEFAULT_TEXT
        defaultSampleShouldNotBeFound("text.doesNotContain=" + DEFAULT_TEXT);

        // Get all the sampleList where text does not contain UPDATED_TEXT
        defaultSampleShouldBeFound("text.doesNotContain=" + UPDATED_TEXT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSampleShouldBeFound(String filter) throws Exception {
        restSampleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sample.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)));

        // Check, that the count call also returns 1
        restSampleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSampleShouldNotBeFound(String filter) throws Exception {
        restSampleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSampleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSample() throws Exception {
        // Get the sample
        restSampleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSample() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        int databaseSizeBeforeUpdate = sampleRepository.findAll().size();

        // Update the sample
        Sample updatedSample = sampleRepository.findById(sample.getId()).get();
        // Disconnect from session so that the updates on updatedSample are not directly saved in db
        em.detach(updatedSample);
        updatedSample.text(UPDATED_TEXT);

        restSampleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSample.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSample))
            )
            .andExpect(status().isOk());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeUpdate);
        Sample testSample = sampleList.get(sampleList.size() - 1);
        assertThat(testSample.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    @Transactional
    void putNonExistingSample() throws Exception {
        int databaseSizeBeforeUpdate = sampleRepository.findAll().size();
        sample.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSampleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sample.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sample))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSample() throws Exception {
        int databaseSizeBeforeUpdate = sampleRepository.findAll().size();
        sample.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSampleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sample))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSample() throws Exception {
        int databaseSizeBeforeUpdate = sampleRepository.findAll().size();
        sample.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSampleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sample)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSampleWithPatch() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        int databaseSizeBeforeUpdate = sampleRepository.findAll().size();

        // Update the sample using partial update
        Sample partialUpdatedSample = new Sample();
        partialUpdatedSample.setId(sample.getId());

        restSampleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSample.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSample))
            )
            .andExpect(status().isOk());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeUpdate);
        Sample testSample = sampleList.get(sampleList.size() - 1);
        assertThat(testSample.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    void fullUpdateSampleWithPatch() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        int databaseSizeBeforeUpdate = sampleRepository.findAll().size();

        // Update the sample using partial update
        Sample partialUpdatedSample = new Sample();
        partialUpdatedSample.setId(sample.getId());

        partialUpdatedSample.text(UPDATED_TEXT);

        restSampleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSample.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSample))
            )
            .andExpect(status().isOk());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeUpdate);
        Sample testSample = sampleList.get(sampleList.size() - 1);
        assertThat(testSample.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    @Transactional
    void patchNonExistingSample() throws Exception {
        int databaseSizeBeforeUpdate = sampleRepository.findAll().size();
        sample.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSampleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sample.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sample))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSample() throws Exception {
        int databaseSizeBeforeUpdate = sampleRepository.findAll().size();
        sample.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSampleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sample))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSample() throws Exception {
        int databaseSizeBeforeUpdate = sampleRepository.findAll().size();
        sample.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSampleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sample)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSample() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        int databaseSizeBeforeDelete = sampleRepository.findAll().size();

        // Delete the sample
        restSampleMockMvc
            .perform(delete(ENTITY_API_URL_ID, sample.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
