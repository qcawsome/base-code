package com.qc.demo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.qc.demo.IntegrationTest;
import com.qc.demo.domain.Device;
import com.qc.demo.domain.IpMac;
import com.qc.demo.repository.IpMacRepository;
import com.qc.demo.service.IpMacService;
import com.qc.demo.service.criteria.IpMacCriteria;
import com.qc.demo.service.dto.IpMacDTO;
import com.qc.demo.service.mapper.IpMacMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IpMacResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class IpMacResourceIT {

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    private static final String DEFAULT_MAC = "AAAAAAAAAA";
    private static final String UPDATED_MAC = "BBBBBBBBBB";

    private static final String DEFAULT_NETWORK_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_NETWORK_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_AGENT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_AGENT_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ip-macs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IpMacRepository ipMacRepository;

    @Mock
    private IpMacRepository ipMacRepositoryMock;

    @Autowired
    private IpMacMapper ipMacMapper;

    @Mock
    private IpMacService ipMacServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIpMacMockMvc;

    private IpMac ipMac;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IpMac createEntity(EntityManager em) {
        IpMac ipMac = new IpMac().ip(DEFAULT_IP).mac(DEFAULT_MAC).networkStatus(DEFAULT_NETWORK_STATUS).agentStatus(DEFAULT_AGENT_STATUS);
        return ipMac;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IpMac createUpdatedEntity(EntityManager em) {
        IpMac ipMac = new IpMac().ip(UPDATED_IP).mac(UPDATED_MAC).networkStatus(UPDATED_NETWORK_STATUS).agentStatus(UPDATED_AGENT_STATUS);
        return ipMac;
    }

    @BeforeEach
    public void initTest() {
        ipMac = createEntity(em);
    }

    @Test
    @Transactional
    void createIpMac() throws Exception {
        int databaseSizeBeforeCreate = ipMacRepository.findAll().size();
        // Create the IpMac
        IpMacDTO ipMacDTO = ipMacMapper.toDto(ipMac);
        restIpMacMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ipMacDTO)))
            .andExpect(status().isCreated());

        // Validate the IpMac in the database
        List<IpMac> ipMacList = ipMacRepository.findAll();
        assertThat(ipMacList).hasSize(databaseSizeBeforeCreate + 1);
        IpMac testIpMac = ipMacList.get(ipMacList.size() - 1);
        assertThat(testIpMac.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testIpMac.getMac()).isEqualTo(DEFAULT_MAC);
        assertThat(testIpMac.getNetworkStatus()).isEqualTo(DEFAULT_NETWORK_STATUS);
        assertThat(testIpMac.getAgentStatus()).isEqualTo(DEFAULT_AGENT_STATUS);
    }

    @Test
    @Transactional
    void createIpMacWithExistingId() throws Exception {
        // Create the IpMac with an existing ID
        ipMac.setId(1L);
        IpMacDTO ipMacDTO = ipMacMapper.toDto(ipMac);

        int databaseSizeBeforeCreate = ipMacRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIpMacMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ipMacDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IpMac in the database
        List<IpMac> ipMacList = ipMacRepository.findAll();
        assertThat(ipMacList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIpMacs() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList
        restIpMacMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ipMac.getId().intValue())))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].mac").value(hasItem(DEFAULT_MAC)))
            .andExpect(jsonPath("$.[*].networkStatus").value(hasItem(DEFAULT_NETWORK_STATUS)))
            .andExpect(jsonPath("$.[*].agentStatus").value(hasItem(DEFAULT_AGENT_STATUS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIpMacsWithEagerRelationshipsIsEnabled() throws Exception {
        when(ipMacServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restIpMacMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ipMacServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIpMacsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ipMacServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restIpMacMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ipMacRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getIpMac() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get the ipMac
        restIpMacMockMvc
            .perform(get(ENTITY_API_URL_ID, ipMac.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ipMac.getId().intValue()))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP))
            .andExpect(jsonPath("$.mac").value(DEFAULT_MAC))
            .andExpect(jsonPath("$.networkStatus").value(DEFAULT_NETWORK_STATUS))
            .andExpect(jsonPath("$.agentStatus").value(DEFAULT_AGENT_STATUS));
    }

    @Test
    @Transactional
    void getIpMacsByIdFiltering() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        Long id = ipMac.getId();

        defaultIpMacShouldBeFound("id.equals=" + id);
        defaultIpMacShouldNotBeFound("id.notEquals=" + id);

        defaultIpMacShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIpMacShouldNotBeFound("id.greaterThan=" + id);

        defaultIpMacShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIpMacShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIpMacsByIpIsEqualToSomething() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where ip equals to DEFAULT_IP
        defaultIpMacShouldBeFound("ip.equals=" + DEFAULT_IP);

        // Get all the ipMacList where ip equals to UPDATED_IP
        defaultIpMacShouldNotBeFound("ip.equals=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllIpMacsByIpIsInShouldWork() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where ip in DEFAULT_IP or UPDATED_IP
        defaultIpMacShouldBeFound("ip.in=" + DEFAULT_IP + "," + UPDATED_IP);

        // Get all the ipMacList where ip equals to UPDATED_IP
        defaultIpMacShouldNotBeFound("ip.in=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllIpMacsByIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where ip is not null
        defaultIpMacShouldBeFound("ip.specified=true");

        // Get all the ipMacList where ip is null
        defaultIpMacShouldNotBeFound("ip.specified=false");
    }

    @Test
    @Transactional
    void getAllIpMacsByIpContainsSomething() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where ip contains DEFAULT_IP
        defaultIpMacShouldBeFound("ip.contains=" + DEFAULT_IP);

        // Get all the ipMacList where ip contains UPDATED_IP
        defaultIpMacShouldNotBeFound("ip.contains=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllIpMacsByIpNotContainsSomething() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where ip does not contain DEFAULT_IP
        defaultIpMacShouldNotBeFound("ip.doesNotContain=" + DEFAULT_IP);

        // Get all the ipMacList where ip does not contain UPDATED_IP
        defaultIpMacShouldBeFound("ip.doesNotContain=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllIpMacsByMacIsEqualToSomething() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where mac equals to DEFAULT_MAC
        defaultIpMacShouldBeFound("mac.equals=" + DEFAULT_MAC);

        // Get all the ipMacList where mac equals to UPDATED_MAC
        defaultIpMacShouldNotBeFound("mac.equals=" + UPDATED_MAC);
    }

    @Test
    @Transactional
    void getAllIpMacsByMacIsInShouldWork() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where mac in DEFAULT_MAC or UPDATED_MAC
        defaultIpMacShouldBeFound("mac.in=" + DEFAULT_MAC + "," + UPDATED_MAC);

        // Get all the ipMacList where mac equals to UPDATED_MAC
        defaultIpMacShouldNotBeFound("mac.in=" + UPDATED_MAC);
    }

    @Test
    @Transactional
    void getAllIpMacsByMacIsNullOrNotNull() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where mac is not null
        defaultIpMacShouldBeFound("mac.specified=true");

        // Get all the ipMacList where mac is null
        defaultIpMacShouldNotBeFound("mac.specified=false");
    }

    @Test
    @Transactional
    void getAllIpMacsByMacContainsSomething() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where mac contains DEFAULT_MAC
        defaultIpMacShouldBeFound("mac.contains=" + DEFAULT_MAC);

        // Get all the ipMacList where mac contains UPDATED_MAC
        defaultIpMacShouldNotBeFound("mac.contains=" + UPDATED_MAC);
    }

    @Test
    @Transactional
    void getAllIpMacsByMacNotContainsSomething() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where mac does not contain DEFAULT_MAC
        defaultIpMacShouldNotBeFound("mac.doesNotContain=" + DEFAULT_MAC);

        // Get all the ipMacList where mac does not contain UPDATED_MAC
        defaultIpMacShouldBeFound("mac.doesNotContain=" + UPDATED_MAC);
    }

    @Test
    @Transactional
    void getAllIpMacsByNetworkStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where networkStatus equals to DEFAULT_NETWORK_STATUS
        defaultIpMacShouldBeFound("networkStatus.equals=" + DEFAULT_NETWORK_STATUS);

        // Get all the ipMacList where networkStatus equals to UPDATED_NETWORK_STATUS
        defaultIpMacShouldNotBeFound("networkStatus.equals=" + UPDATED_NETWORK_STATUS);
    }

    @Test
    @Transactional
    void getAllIpMacsByNetworkStatusIsInShouldWork() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where networkStatus in DEFAULT_NETWORK_STATUS or UPDATED_NETWORK_STATUS
        defaultIpMacShouldBeFound("networkStatus.in=" + DEFAULT_NETWORK_STATUS + "," + UPDATED_NETWORK_STATUS);

        // Get all the ipMacList where networkStatus equals to UPDATED_NETWORK_STATUS
        defaultIpMacShouldNotBeFound("networkStatus.in=" + UPDATED_NETWORK_STATUS);
    }

    @Test
    @Transactional
    void getAllIpMacsByNetworkStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where networkStatus is not null
        defaultIpMacShouldBeFound("networkStatus.specified=true");

        // Get all the ipMacList where networkStatus is null
        defaultIpMacShouldNotBeFound("networkStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllIpMacsByNetworkStatusContainsSomething() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where networkStatus contains DEFAULT_NETWORK_STATUS
        defaultIpMacShouldBeFound("networkStatus.contains=" + DEFAULT_NETWORK_STATUS);

        // Get all the ipMacList where networkStatus contains UPDATED_NETWORK_STATUS
        defaultIpMacShouldNotBeFound("networkStatus.contains=" + UPDATED_NETWORK_STATUS);
    }

    @Test
    @Transactional
    void getAllIpMacsByNetworkStatusNotContainsSomething() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where networkStatus does not contain DEFAULT_NETWORK_STATUS
        defaultIpMacShouldNotBeFound("networkStatus.doesNotContain=" + DEFAULT_NETWORK_STATUS);

        // Get all the ipMacList where networkStatus does not contain UPDATED_NETWORK_STATUS
        defaultIpMacShouldBeFound("networkStatus.doesNotContain=" + UPDATED_NETWORK_STATUS);
    }

    @Test
    @Transactional
    void getAllIpMacsByAgentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where agentStatus equals to DEFAULT_AGENT_STATUS
        defaultIpMacShouldBeFound("agentStatus.equals=" + DEFAULT_AGENT_STATUS);

        // Get all the ipMacList where agentStatus equals to UPDATED_AGENT_STATUS
        defaultIpMacShouldNotBeFound("agentStatus.equals=" + UPDATED_AGENT_STATUS);
    }

    @Test
    @Transactional
    void getAllIpMacsByAgentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where agentStatus in DEFAULT_AGENT_STATUS or UPDATED_AGENT_STATUS
        defaultIpMacShouldBeFound("agentStatus.in=" + DEFAULT_AGENT_STATUS + "," + UPDATED_AGENT_STATUS);

        // Get all the ipMacList where agentStatus equals to UPDATED_AGENT_STATUS
        defaultIpMacShouldNotBeFound("agentStatus.in=" + UPDATED_AGENT_STATUS);
    }

    @Test
    @Transactional
    void getAllIpMacsByAgentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where agentStatus is not null
        defaultIpMacShouldBeFound("agentStatus.specified=true");

        // Get all the ipMacList where agentStatus is null
        defaultIpMacShouldNotBeFound("agentStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllIpMacsByAgentStatusContainsSomething() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where agentStatus contains DEFAULT_AGENT_STATUS
        defaultIpMacShouldBeFound("agentStatus.contains=" + DEFAULT_AGENT_STATUS);

        // Get all the ipMacList where agentStatus contains UPDATED_AGENT_STATUS
        defaultIpMacShouldNotBeFound("agentStatus.contains=" + UPDATED_AGENT_STATUS);
    }

    @Test
    @Transactional
    void getAllIpMacsByAgentStatusNotContainsSomething() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        // Get all the ipMacList where agentStatus does not contain DEFAULT_AGENT_STATUS
        defaultIpMacShouldNotBeFound("agentStatus.doesNotContain=" + DEFAULT_AGENT_STATUS);

        // Get all the ipMacList where agentStatus does not contain UPDATED_AGENT_STATUS
        defaultIpMacShouldBeFound("agentStatus.doesNotContain=" + UPDATED_AGENT_STATUS);
    }

    @Test
    @Transactional
    void getAllIpMacsByDeviceIsEqualToSomething() throws Exception {
        Device device;
        if (TestUtil.findAll(em, Device.class).isEmpty()) {
            ipMacRepository.saveAndFlush(ipMac);
            device = DeviceResourceIT.createEntity(em);
        } else {
            device = TestUtil.findAll(em, Device.class).get(0);
        }
        em.persist(device);
        em.flush();
        ipMac.setDevice(device);
        ipMacRepository.saveAndFlush(ipMac);
        Long deviceId = device.getId();

        // Get all the ipMacList where device equals to deviceId
        defaultIpMacShouldBeFound("deviceId.equals=" + deviceId);

        // Get all the ipMacList where device equals to (deviceId + 1)
        defaultIpMacShouldNotBeFound("deviceId.equals=" + (deviceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIpMacShouldBeFound(String filter) throws Exception {
        restIpMacMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ipMac.getId().intValue())))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].mac").value(hasItem(DEFAULT_MAC)))
            .andExpect(jsonPath("$.[*].networkStatus").value(hasItem(DEFAULT_NETWORK_STATUS)))
            .andExpect(jsonPath("$.[*].agentStatus").value(hasItem(DEFAULT_AGENT_STATUS)));

        // Check, that the count call also returns 1
        restIpMacMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIpMacShouldNotBeFound(String filter) throws Exception {
        restIpMacMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIpMacMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIpMac() throws Exception {
        // Get the ipMac
        restIpMacMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIpMac() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        int databaseSizeBeforeUpdate = ipMacRepository.findAll().size();

        // Update the ipMac
        IpMac updatedIpMac = ipMacRepository.findById(ipMac.getId()).get();
        // Disconnect from session so that the updates on updatedIpMac are not directly saved in db
        em.detach(updatedIpMac);
        updatedIpMac.ip(UPDATED_IP).mac(UPDATED_MAC).networkStatus(UPDATED_NETWORK_STATUS).agentStatus(UPDATED_AGENT_STATUS);
        IpMacDTO ipMacDTO = ipMacMapper.toDto(updatedIpMac);

        restIpMacMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ipMacDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ipMacDTO))
            )
            .andExpect(status().isOk());

        // Validate the IpMac in the database
        List<IpMac> ipMacList = ipMacRepository.findAll();
        assertThat(ipMacList).hasSize(databaseSizeBeforeUpdate);
        IpMac testIpMac = ipMacList.get(ipMacList.size() - 1);
        assertThat(testIpMac.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testIpMac.getMac()).isEqualTo(UPDATED_MAC);
        assertThat(testIpMac.getNetworkStatus()).isEqualTo(UPDATED_NETWORK_STATUS);
        assertThat(testIpMac.getAgentStatus()).isEqualTo(UPDATED_AGENT_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingIpMac() throws Exception {
        int databaseSizeBeforeUpdate = ipMacRepository.findAll().size();
        ipMac.setId(count.incrementAndGet());

        // Create the IpMac
        IpMacDTO ipMacDTO = ipMacMapper.toDto(ipMac);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIpMacMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ipMacDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ipMacDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IpMac in the database
        List<IpMac> ipMacList = ipMacRepository.findAll();
        assertThat(ipMacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIpMac() throws Exception {
        int databaseSizeBeforeUpdate = ipMacRepository.findAll().size();
        ipMac.setId(count.incrementAndGet());

        // Create the IpMac
        IpMacDTO ipMacDTO = ipMacMapper.toDto(ipMac);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIpMacMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ipMacDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IpMac in the database
        List<IpMac> ipMacList = ipMacRepository.findAll();
        assertThat(ipMacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIpMac() throws Exception {
        int databaseSizeBeforeUpdate = ipMacRepository.findAll().size();
        ipMac.setId(count.incrementAndGet());

        // Create the IpMac
        IpMacDTO ipMacDTO = ipMacMapper.toDto(ipMac);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIpMacMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ipMacDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IpMac in the database
        List<IpMac> ipMacList = ipMacRepository.findAll();
        assertThat(ipMacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIpMacWithPatch() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        int databaseSizeBeforeUpdate = ipMacRepository.findAll().size();

        // Update the ipMac using partial update
        IpMac partialUpdatedIpMac = new IpMac();
        partialUpdatedIpMac.setId(ipMac.getId());

        partialUpdatedIpMac.mac(UPDATED_MAC).networkStatus(UPDATED_NETWORK_STATUS).agentStatus(UPDATED_AGENT_STATUS);

        restIpMacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIpMac.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIpMac))
            )
            .andExpect(status().isOk());

        // Validate the IpMac in the database
        List<IpMac> ipMacList = ipMacRepository.findAll();
        assertThat(ipMacList).hasSize(databaseSizeBeforeUpdate);
        IpMac testIpMac = ipMacList.get(ipMacList.size() - 1);
        assertThat(testIpMac.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testIpMac.getMac()).isEqualTo(UPDATED_MAC);
        assertThat(testIpMac.getNetworkStatus()).isEqualTo(UPDATED_NETWORK_STATUS);
        assertThat(testIpMac.getAgentStatus()).isEqualTo(UPDATED_AGENT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateIpMacWithPatch() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        int databaseSizeBeforeUpdate = ipMacRepository.findAll().size();

        // Update the ipMac using partial update
        IpMac partialUpdatedIpMac = new IpMac();
        partialUpdatedIpMac.setId(ipMac.getId());

        partialUpdatedIpMac.ip(UPDATED_IP).mac(UPDATED_MAC).networkStatus(UPDATED_NETWORK_STATUS).agentStatus(UPDATED_AGENT_STATUS);

        restIpMacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIpMac.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIpMac))
            )
            .andExpect(status().isOk());

        // Validate the IpMac in the database
        List<IpMac> ipMacList = ipMacRepository.findAll();
        assertThat(ipMacList).hasSize(databaseSizeBeforeUpdate);
        IpMac testIpMac = ipMacList.get(ipMacList.size() - 1);
        assertThat(testIpMac.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testIpMac.getMac()).isEqualTo(UPDATED_MAC);
        assertThat(testIpMac.getNetworkStatus()).isEqualTo(UPDATED_NETWORK_STATUS);
        assertThat(testIpMac.getAgentStatus()).isEqualTo(UPDATED_AGENT_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingIpMac() throws Exception {
        int databaseSizeBeforeUpdate = ipMacRepository.findAll().size();
        ipMac.setId(count.incrementAndGet());

        // Create the IpMac
        IpMacDTO ipMacDTO = ipMacMapper.toDto(ipMac);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIpMacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ipMacDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ipMacDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IpMac in the database
        List<IpMac> ipMacList = ipMacRepository.findAll();
        assertThat(ipMacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIpMac() throws Exception {
        int databaseSizeBeforeUpdate = ipMacRepository.findAll().size();
        ipMac.setId(count.incrementAndGet());

        // Create the IpMac
        IpMacDTO ipMacDTO = ipMacMapper.toDto(ipMac);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIpMacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ipMacDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IpMac in the database
        List<IpMac> ipMacList = ipMacRepository.findAll();
        assertThat(ipMacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIpMac() throws Exception {
        int databaseSizeBeforeUpdate = ipMacRepository.findAll().size();
        ipMac.setId(count.incrementAndGet());

        // Create the IpMac
        IpMacDTO ipMacDTO = ipMacMapper.toDto(ipMac);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIpMacMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ipMacDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IpMac in the database
        List<IpMac> ipMacList = ipMacRepository.findAll();
        assertThat(ipMacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIpMac() throws Exception {
        // Initialize the database
        ipMacRepository.saveAndFlush(ipMac);

        int databaseSizeBeforeDelete = ipMacRepository.findAll().size();

        // Delete the ipMac
        restIpMacMockMvc
            .perform(delete(ENTITY_API_URL_ID, ipMac.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IpMac> ipMacList = ipMacRepository.findAll();
        assertThat(ipMacList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
