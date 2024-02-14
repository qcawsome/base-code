package com.qc.demo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.qc.demo.IntegrationTest;
import com.qc.demo.domain.Device;
import com.qc.demo.domain.IpMac;
import com.qc.demo.repository.DeviceRepository;
import com.qc.demo.service.criteria.DeviceCriteria;
import com.qc.demo.service.dto.DeviceDTO;
import com.qc.demo.service.mapper.DeviceMapper;
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
 * Integration tests for the {@link DeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeviceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AGENT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_AGENT_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_NETWORK_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_NETWORK_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceMockMvc;

    private Device device;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createEntity(EntityManager em) {
        Device device = new Device().name(DEFAULT_NAME).agentStatus(DEFAULT_AGENT_STATUS).networkStatus(DEFAULT_NETWORK_STATUS);
        return device;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createUpdatedEntity(EntityManager em) {
        Device device = new Device().name(UPDATED_NAME).agentStatus(UPDATED_AGENT_STATUS).networkStatus(UPDATED_NETWORK_STATUS);
        return device;
    }

    @BeforeEach
    public void initTest() {
        device = createEntity(em);
    }

    @Test
    @Transactional
    void createDevice() throws Exception {
        int databaseSizeBeforeCreate = deviceRepository.findAll().size();
        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);
        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isCreated());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate + 1);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDevice.getAgentStatus()).isEqualTo(DEFAULT_AGENT_STATUS);
        assertThat(testDevice.getNetworkStatus()).isEqualTo(DEFAULT_NETWORK_STATUS);
    }

    @Test
    @Transactional
    void createDeviceWithExistingId() throws Exception {
        // Create the Device with an existing ID
        device.setId(1L);
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        int databaseSizeBeforeCreate = deviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDevices() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].agentStatus").value(hasItem(DEFAULT_AGENT_STATUS)))
            .andExpect(jsonPath("$.[*].networkStatus").value(hasItem(DEFAULT_NETWORK_STATUS)));
    }

    @Test
    @Transactional
    void getDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get the device
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, device.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(device.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.agentStatus").value(DEFAULT_AGENT_STATUS))
            .andExpect(jsonPath("$.networkStatus").value(DEFAULT_NETWORK_STATUS));
    }

    @Test
    @Transactional
    void getDevicesByIdFiltering() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        Long id = device.getId();

        defaultDeviceShouldBeFound("id.equals=" + id);
        defaultDeviceShouldNotBeFound("id.notEquals=" + id);

        defaultDeviceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDeviceShouldNotBeFound("id.greaterThan=" + id);

        defaultDeviceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDeviceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDevicesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where name equals to DEFAULT_NAME
        defaultDeviceShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the deviceList where name equals to UPDATED_NAME
        defaultDeviceShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDevicesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDeviceShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the deviceList where name equals to UPDATED_NAME
        defaultDeviceShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDevicesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where name is not null
        defaultDeviceShouldBeFound("name.specified=true");

        // Get all the deviceList where name is null
        defaultDeviceShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByNameContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where name contains DEFAULT_NAME
        defaultDeviceShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the deviceList where name contains UPDATED_NAME
        defaultDeviceShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDevicesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where name does not contain DEFAULT_NAME
        defaultDeviceShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the deviceList where name does not contain UPDATED_NAME
        defaultDeviceShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDevicesByAgentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where agentStatus equals to DEFAULT_AGENT_STATUS
        defaultDeviceShouldBeFound("agentStatus.equals=" + DEFAULT_AGENT_STATUS);

        // Get all the deviceList where agentStatus equals to UPDATED_AGENT_STATUS
        defaultDeviceShouldNotBeFound("agentStatus.equals=" + UPDATED_AGENT_STATUS);
    }

    @Test
    @Transactional
    void getAllDevicesByAgentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where agentStatus in DEFAULT_AGENT_STATUS or UPDATED_AGENT_STATUS
        defaultDeviceShouldBeFound("agentStatus.in=" + DEFAULT_AGENT_STATUS + "," + UPDATED_AGENT_STATUS);

        // Get all the deviceList where agentStatus equals to UPDATED_AGENT_STATUS
        defaultDeviceShouldNotBeFound("agentStatus.in=" + UPDATED_AGENT_STATUS);
    }

    @Test
    @Transactional
    void getAllDevicesByAgentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where agentStatus is not null
        defaultDeviceShouldBeFound("agentStatus.specified=true");

        // Get all the deviceList where agentStatus is null
        defaultDeviceShouldNotBeFound("agentStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByAgentStatusContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where agentStatus contains DEFAULT_AGENT_STATUS
        defaultDeviceShouldBeFound("agentStatus.contains=" + DEFAULT_AGENT_STATUS);

        // Get all the deviceList where agentStatus contains UPDATED_AGENT_STATUS
        defaultDeviceShouldNotBeFound("agentStatus.contains=" + UPDATED_AGENT_STATUS);
    }

    @Test
    @Transactional
    void getAllDevicesByAgentStatusNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where agentStatus does not contain DEFAULT_AGENT_STATUS
        defaultDeviceShouldNotBeFound("agentStatus.doesNotContain=" + DEFAULT_AGENT_STATUS);

        // Get all the deviceList where agentStatus does not contain UPDATED_AGENT_STATUS
        defaultDeviceShouldBeFound("agentStatus.doesNotContain=" + UPDATED_AGENT_STATUS);
    }

    @Test
    @Transactional
    void getAllDevicesByNetworkStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where networkStatus equals to DEFAULT_NETWORK_STATUS
        defaultDeviceShouldBeFound("networkStatus.equals=" + DEFAULT_NETWORK_STATUS);

        // Get all the deviceList where networkStatus equals to UPDATED_NETWORK_STATUS
        defaultDeviceShouldNotBeFound("networkStatus.equals=" + UPDATED_NETWORK_STATUS);
    }

    @Test
    @Transactional
    void getAllDevicesByNetworkStatusIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where networkStatus in DEFAULT_NETWORK_STATUS or UPDATED_NETWORK_STATUS
        defaultDeviceShouldBeFound("networkStatus.in=" + DEFAULT_NETWORK_STATUS + "," + UPDATED_NETWORK_STATUS);

        // Get all the deviceList where networkStatus equals to UPDATED_NETWORK_STATUS
        defaultDeviceShouldNotBeFound("networkStatus.in=" + UPDATED_NETWORK_STATUS);
    }

    @Test
    @Transactional
    void getAllDevicesByNetworkStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where networkStatus is not null
        defaultDeviceShouldBeFound("networkStatus.specified=true");

        // Get all the deviceList where networkStatus is null
        defaultDeviceShouldNotBeFound("networkStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByNetworkStatusContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where networkStatus contains DEFAULT_NETWORK_STATUS
        defaultDeviceShouldBeFound("networkStatus.contains=" + DEFAULT_NETWORK_STATUS);

        // Get all the deviceList where networkStatus contains UPDATED_NETWORK_STATUS
        defaultDeviceShouldNotBeFound("networkStatus.contains=" + UPDATED_NETWORK_STATUS);
    }

    @Test
    @Transactional
    void getAllDevicesByNetworkStatusNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where networkStatus does not contain DEFAULT_NETWORK_STATUS
        defaultDeviceShouldNotBeFound("networkStatus.doesNotContain=" + DEFAULT_NETWORK_STATUS);

        // Get all the deviceList where networkStatus does not contain UPDATED_NETWORK_STATUS
        defaultDeviceShouldBeFound("networkStatus.doesNotContain=" + UPDATED_NETWORK_STATUS);
    }

    @Test
    @Transactional
    void getAllDevicesByIpMacsIsEqualToSomething() throws Exception {
        IpMac ipMacs;
        if (TestUtil.findAll(em, IpMac.class).isEmpty()) {
            deviceRepository.saveAndFlush(device);
            ipMacs = IpMacResourceIT.createEntity(em);
        } else {
            ipMacs = TestUtil.findAll(em, IpMac.class).get(0);
        }
        em.persist(ipMacs);
        em.flush();
        device.addIpMacs(ipMacs);
        deviceRepository.saveAndFlush(device);
        Long ipMacsId = ipMacs.getId();

        // Get all the deviceList where ipMacs equals to ipMacsId
        defaultDeviceShouldBeFound("ipMacsId.equals=" + ipMacsId);

        // Get all the deviceList where ipMacs equals to (ipMacsId + 1)
        defaultDeviceShouldNotBeFound("ipMacsId.equals=" + (ipMacsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeviceShouldBeFound(String filter) throws Exception {
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].agentStatus").value(hasItem(DEFAULT_AGENT_STATUS)))
            .andExpect(jsonPath("$.[*].networkStatus").value(hasItem(DEFAULT_NETWORK_STATUS)));

        // Check, that the count call also returns 1
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeviceShouldNotBeFound(String filter) throws Exception {
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDevice() throws Exception {
        // Get the device
        restDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device
        Device updatedDevice = deviceRepository.findById(device.getId()).get();
        // Disconnect from session so that the updates on updatedDevice are not directly saved in db
        em.detach(updatedDevice);
        updatedDevice.name(UPDATED_NAME).agentStatus(UPDATED_AGENT_STATUS).networkStatus(UPDATED_NETWORK_STATUS);
        DeviceDTO deviceDTO = deviceMapper.toDto(updatedDevice);

        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDevice.getAgentStatus()).isEqualTo(UPDATED_AGENT_STATUS);
        assertThat(testDevice.getNetworkStatus()).isEqualTo(UPDATED_NETWORK_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDevice.getAgentStatus()).isEqualTo(DEFAULT_AGENT_STATUS);
        assertThat(testDevice.getNetworkStatus()).isEqualTo(DEFAULT_NETWORK_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice.name(UPDATED_NAME).agentStatus(UPDATED_AGENT_STATUS).networkStatus(UPDATED_NETWORK_STATUS);

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDevice.getAgentStatus()).isEqualTo(UPDATED_AGENT_STATUS);
        assertThat(testDevice.getNetworkStatus()).isEqualTo(UPDATED_NETWORK_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeDelete = deviceRepository.findAll().size();

        // Delete the device
        restDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, device.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
