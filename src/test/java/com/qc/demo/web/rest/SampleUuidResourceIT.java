package com.qc.demo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.qc.demo.IntegrationTest;
import com.qc.demo.domain.SampleUuid;
import com.qc.demo.repository.SampleUuidRepository;
import com.qc.demo.service.criteria.SampleUuidCriteria;
import com.qc.demo.service.dto.SampleUuidDTO;
import com.qc.demo.service.mapper.SampleUuidMapper;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link SampleUuidResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SampleUuidResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/sample-uuids";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SampleUuidRepository sampleUuidRepository;

    @Autowired
    private SampleUuidMapper sampleUuidMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSampleUuidMockMvc;

    private SampleUuid sampleUuid;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SampleUuid createEntity(EntityManager em) {
        SampleUuid sampleUuid = new SampleUuid().uuid(DEFAULT_UUID);
        return sampleUuid;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SampleUuid createUpdatedEntity(EntityManager em) {
        SampleUuid sampleUuid = new SampleUuid().uuid(UPDATED_UUID);
        return sampleUuid;
    }

    @BeforeEach
    public void initTest() {
        sampleUuid = createEntity(em);
    }

    @Test
    @Transactional
    void createSampleUuid() throws Exception {
        int databaseSizeBeforeCreate = sampleUuidRepository.findAll().size();
        // Create the SampleUuid
        SampleUuidDTO sampleUuidDTO = sampleUuidMapper.toDto(sampleUuid);
        restSampleUuidMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sampleUuidDTO)))
            .andExpect(status().isCreated());

        // Validate the SampleUuid in the database
        List<SampleUuid> sampleUuidList = sampleUuidRepository.findAll();
        assertThat(sampleUuidList).hasSize(databaseSizeBeforeCreate + 1);
        SampleUuid testSampleUuid = sampleUuidList.get(sampleUuidList.size() - 1);
        assertThat(testSampleUuid.getUuid()).isEqualTo(DEFAULT_UUID);
    }

    @Test
    @Transactional
    void createSampleUuidWithExistingId() throws Exception {
        // Create the SampleUuid with an existing ID
        sampleUuid.setId(1L);
        SampleUuidDTO sampleUuidDTO = sampleUuidMapper.toDto(sampleUuid);

        int databaseSizeBeforeCreate = sampleUuidRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSampleUuidMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sampleUuidDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SampleUuid in the database
        List<SampleUuid> sampleUuidList = sampleUuidRepository.findAll();
        assertThat(sampleUuidList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSampleUuids() throws Exception {
        // Initialize the database
        sampleUuidRepository.saveAndFlush(sampleUuid);

        // Get all the sampleUuidList
        restSampleUuidMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sampleUuid.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())));
    }

    @Test
    @Transactional
    void getSampleUuid() throws Exception {
        // Initialize the database
        sampleUuidRepository.saveAndFlush(sampleUuid);

        // Get the sampleUuid
        restSampleUuidMockMvc
            .perform(get(ENTITY_API_URL_ID, sampleUuid.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sampleUuid.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()));
    }

    @Test
    @Transactional
    void getSampleUuidsByIdFiltering() throws Exception {
        // Initialize the database
        sampleUuidRepository.saveAndFlush(sampleUuid);

        Long id = sampleUuid.getId();

        defaultSampleUuidShouldBeFound("id.equals=" + id);
        defaultSampleUuidShouldNotBeFound("id.notEquals=" + id);

        defaultSampleUuidShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSampleUuidShouldNotBeFound("id.greaterThan=" + id);

        defaultSampleUuidShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSampleUuidShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSampleUuidsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        sampleUuidRepository.saveAndFlush(sampleUuid);

        // Get all the sampleUuidList where uuid equals to DEFAULT_UUID
        defaultSampleUuidShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the sampleUuidList where uuid equals to UPDATED_UUID
        defaultSampleUuidShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllSampleUuidsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        sampleUuidRepository.saveAndFlush(sampleUuid);

        // Get all the sampleUuidList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultSampleUuidShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the sampleUuidList where uuid equals to UPDATED_UUID
        defaultSampleUuidShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllSampleUuidsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        sampleUuidRepository.saveAndFlush(sampleUuid);

        // Get all the sampleUuidList where uuid is not null
        defaultSampleUuidShouldBeFound("uuid.specified=true");

        // Get all the sampleUuidList where uuid is null
        defaultSampleUuidShouldNotBeFound("uuid.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSampleUuidShouldBeFound(String filter) throws Exception {
        restSampleUuidMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sampleUuid.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())));

        // Check, that the count call also returns 1
        restSampleUuidMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSampleUuidShouldNotBeFound(String filter) throws Exception {
        restSampleUuidMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSampleUuidMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSampleUuid() throws Exception {
        // Get the sampleUuid
        restSampleUuidMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSampleUuid() throws Exception {
        // Initialize the database
        sampleUuidRepository.saveAndFlush(sampleUuid);

        int databaseSizeBeforeUpdate = sampleUuidRepository.findAll().size();

        // Update the sampleUuid
        SampleUuid updatedSampleUuid = sampleUuidRepository.findById(sampleUuid.getId()).get();
        // Disconnect from session so that the updates on updatedSampleUuid are not directly saved in db
        em.detach(updatedSampleUuid);
        updatedSampleUuid.uuid(UPDATED_UUID);
        SampleUuidDTO sampleUuidDTO = sampleUuidMapper.toDto(updatedSampleUuid);

        restSampleUuidMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sampleUuidDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sampleUuidDTO))
            )
            .andExpect(status().isOk());

        // Validate the SampleUuid in the database
        List<SampleUuid> sampleUuidList = sampleUuidRepository.findAll();
        assertThat(sampleUuidList).hasSize(databaseSizeBeforeUpdate);
        SampleUuid testSampleUuid = sampleUuidList.get(sampleUuidList.size() - 1);
        assertThat(testSampleUuid.getUuid()).isEqualTo(UPDATED_UUID);
    }

    @Test
    @Transactional
    void putNonExistingSampleUuid() throws Exception {
        int databaseSizeBeforeUpdate = sampleUuidRepository.findAll().size();
        sampleUuid.setId(count.incrementAndGet());

        // Create the SampleUuid
        SampleUuidDTO sampleUuidDTO = sampleUuidMapper.toDto(sampleUuid);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSampleUuidMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sampleUuidDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sampleUuidDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SampleUuid in the database
        List<SampleUuid> sampleUuidList = sampleUuidRepository.findAll();
        assertThat(sampleUuidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSampleUuid() throws Exception {
        int databaseSizeBeforeUpdate = sampleUuidRepository.findAll().size();
        sampleUuid.setId(count.incrementAndGet());

        // Create the SampleUuid
        SampleUuidDTO sampleUuidDTO = sampleUuidMapper.toDto(sampleUuid);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSampleUuidMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sampleUuidDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SampleUuid in the database
        List<SampleUuid> sampleUuidList = sampleUuidRepository.findAll();
        assertThat(sampleUuidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSampleUuid() throws Exception {
        int databaseSizeBeforeUpdate = sampleUuidRepository.findAll().size();
        sampleUuid.setId(count.incrementAndGet());

        // Create the SampleUuid
        SampleUuidDTO sampleUuidDTO = sampleUuidMapper.toDto(sampleUuid);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSampleUuidMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sampleUuidDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SampleUuid in the database
        List<SampleUuid> sampleUuidList = sampleUuidRepository.findAll();
        assertThat(sampleUuidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSampleUuidWithPatch() throws Exception {
        // Initialize the database
        sampleUuidRepository.saveAndFlush(sampleUuid);

        int databaseSizeBeforeUpdate = sampleUuidRepository.findAll().size();

        // Update the sampleUuid using partial update
        SampleUuid partialUpdatedSampleUuid = new SampleUuid();
        partialUpdatedSampleUuid.setId(sampleUuid.getId());

        partialUpdatedSampleUuid.uuid(UPDATED_UUID);

        restSampleUuidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSampleUuid.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSampleUuid))
            )
            .andExpect(status().isOk());

        // Validate the SampleUuid in the database
        List<SampleUuid> sampleUuidList = sampleUuidRepository.findAll();
        assertThat(sampleUuidList).hasSize(databaseSizeBeforeUpdate);
        SampleUuid testSampleUuid = sampleUuidList.get(sampleUuidList.size() - 1);
        assertThat(testSampleUuid.getUuid()).isEqualTo(UPDATED_UUID);
    }

    @Test
    @Transactional
    void fullUpdateSampleUuidWithPatch() throws Exception {
        // Initialize the database
        sampleUuidRepository.saveAndFlush(sampleUuid);

        int databaseSizeBeforeUpdate = sampleUuidRepository.findAll().size();

        // Update the sampleUuid using partial update
        SampleUuid partialUpdatedSampleUuid = new SampleUuid();
        partialUpdatedSampleUuid.setId(sampleUuid.getId());

        partialUpdatedSampleUuid.uuid(UPDATED_UUID);

        restSampleUuidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSampleUuid.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSampleUuid))
            )
            .andExpect(status().isOk());

        // Validate the SampleUuid in the database
        List<SampleUuid> sampleUuidList = sampleUuidRepository.findAll();
        assertThat(sampleUuidList).hasSize(databaseSizeBeforeUpdate);
        SampleUuid testSampleUuid = sampleUuidList.get(sampleUuidList.size() - 1);
        assertThat(testSampleUuid.getUuid()).isEqualTo(UPDATED_UUID);
    }

    @Test
    @Transactional
    void patchNonExistingSampleUuid() throws Exception {
        int databaseSizeBeforeUpdate = sampleUuidRepository.findAll().size();
        sampleUuid.setId(count.incrementAndGet());

        // Create the SampleUuid
        SampleUuidDTO sampleUuidDTO = sampleUuidMapper.toDto(sampleUuid);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSampleUuidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sampleUuidDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sampleUuidDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SampleUuid in the database
        List<SampleUuid> sampleUuidList = sampleUuidRepository.findAll();
        assertThat(sampleUuidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSampleUuid() throws Exception {
        int databaseSizeBeforeUpdate = sampleUuidRepository.findAll().size();
        sampleUuid.setId(count.incrementAndGet());

        // Create the SampleUuid
        SampleUuidDTO sampleUuidDTO = sampleUuidMapper.toDto(sampleUuid);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSampleUuidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sampleUuidDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SampleUuid in the database
        List<SampleUuid> sampleUuidList = sampleUuidRepository.findAll();
        assertThat(sampleUuidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSampleUuid() throws Exception {
        int databaseSizeBeforeUpdate = sampleUuidRepository.findAll().size();
        sampleUuid.setId(count.incrementAndGet());

        // Create the SampleUuid
        SampleUuidDTO sampleUuidDTO = sampleUuidMapper.toDto(sampleUuid);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSampleUuidMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sampleUuidDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SampleUuid in the database
        List<SampleUuid> sampleUuidList = sampleUuidRepository.findAll();
        assertThat(sampleUuidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSampleUuid() throws Exception {
        // Initialize the database
        sampleUuidRepository.saveAndFlush(sampleUuid);

        int databaseSizeBeforeDelete = sampleUuidRepository.findAll().size();

        // Delete the sampleUuid
        restSampleUuidMockMvc
            .perform(delete(ENTITY_API_URL_ID, sampleUuid.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SampleUuid> sampleUuidList = sampleUuidRepository.findAll();
        assertThat(sampleUuidList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
