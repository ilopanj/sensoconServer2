package com.sensocon.server.web.rest;

import com.sensocon.server.SensoconServer2App;

import com.sensocon.server.domain.SensorDevice;
import com.sensocon.server.repository.SensorDeviceRepository;
import com.sensocon.server.service.SensorDeviceService;
import com.sensocon.server.service.dto.SensorDeviceDTO;
import com.sensocon.server.service.mapper.SensorDeviceMapper;
import com.sensocon.server.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.sensocon.server.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SensorDeviceResource REST controller.
 *
 * @see SensorDeviceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SensoconServer2App.class)
public class SensorDeviceResourceIntTest {

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private SensorDeviceRepository sensorDeviceRepository;


    @Autowired
    private SensorDeviceMapper sensorDeviceMapper;
    

    @Autowired
    private SensorDeviceService sensorDeviceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSensorDeviceMockMvc;

    private SensorDevice sensorDevice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SensorDeviceResource sensorDeviceResource = new SensorDeviceResource(sensorDeviceService);
        this.restSensorDeviceMockMvc = MockMvcBuilders.standaloneSetup(sensorDeviceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SensorDevice createEntity(EntityManager em) {
        SensorDevice sensorDevice = new SensorDevice()
            .deviceId(DEFAULT_DEVICE_ID)
            .name(DEFAULT_NAME);
        return sensorDevice;
    }

    @Before
    public void initTest() {
        sensorDevice = createEntity(em);
    }

    @Test
    @Transactional
    public void createSensorDevice() throws Exception {
        int databaseSizeBeforeCreate = sensorDeviceRepository.findAll().size();

        // Create the SensorDevice
        SensorDeviceDTO sensorDeviceDTO = sensorDeviceMapper.toDto(sensorDevice);
        restSensorDeviceMockMvc.perform(post("/api/sensor-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorDeviceDTO)))
            .andExpect(status().isCreated());

        // Validate the SensorDevice in the database
        List<SensorDevice> sensorDeviceList = sensorDeviceRepository.findAll();
        assertThat(sensorDeviceList).hasSize(databaseSizeBeforeCreate + 1);
        SensorDevice testSensorDevice = sensorDeviceList.get(sensorDeviceList.size() - 1);
        assertThat(testSensorDevice.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testSensorDevice.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSensorDeviceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sensorDeviceRepository.findAll().size();

        // Create the SensorDevice with an existing ID
        sensorDevice.setId(1L);
        SensorDeviceDTO sensorDeviceDTO = sensorDeviceMapper.toDto(sensorDevice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensorDeviceMockMvc.perform(post("/api/sensor-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorDeviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SensorDevice in the database
        List<SensorDevice> sensorDeviceList = sensorDeviceRepository.findAll();
        assertThat(sensorDeviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDeviceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = sensorDeviceRepository.findAll().size();
        // set the field null
        sensorDevice.setDeviceId(null);

        // Create the SensorDevice, which fails.
        SensorDeviceDTO sensorDeviceDTO = sensorDeviceMapper.toDto(sensorDevice);

        restSensorDeviceMockMvc.perform(post("/api/sensor-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorDeviceDTO)))
            .andExpect(status().isBadRequest());

        List<SensorDevice> sensorDeviceList = sensorDeviceRepository.findAll();
        assertThat(sensorDeviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSensorDevices() throws Exception {
        // Initialize the database
        sensorDeviceRepository.saveAndFlush(sensorDevice);

        // Get all the sensorDeviceList
        restSensorDeviceMockMvc.perform(get("/api/sensor-devices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensorDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    

    @Test
    @Transactional
    public void getSensorDevice() throws Exception {
        // Initialize the database
        sensorDeviceRepository.saveAndFlush(sensorDevice);

        // Get the sensorDevice
        restSensorDeviceMockMvc.perform(get("/api/sensor-devices/{id}", sensorDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sensorDevice.getId().intValue()))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSensorDevice() throws Exception {
        // Get the sensorDevice
        restSensorDeviceMockMvc.perform(get("/api/sensor-devices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSensorDevice() throws Exception {
        // Initialize the database
        sensorDeviceRepository.saveAndFlush(sensorDevice);

        int databaseSizeBeforeUpdate = sensorDeviceRepository.findAll().size();

        // Update the sensorDevice
        SensorDevice updatedSensorDevice = sensorDeviceRepository.findById(sensorDevice.getId()).get();
        // Disconnect from session so that the updates on updatedSensorDevice are not directly saved in db
        em.detach(updatedSensorDevice);
        updatedSensorDevice
            .deviceId(UPDATED_DEVICE_ID)
            .name(UPDATED_NAME);
        SensorDeviceDTO sensorDeviceDTO = sensorDeviceMapper.toDto(updatedSensorDevice);

        restSensorDeviceMockMvc.perform(put("/api/sensor-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorDeviceDTO)))
            .andExpect(status().isOk());

        // Validate the SensorDevice in the database
        List<SensorDevice> sensorDeviceList = sensorDeviceRepository.findAll();
        assertThat(sensorDeviceList).hasSize(databaseSizeBeforeUpdate);
        SensorDevice testSensorDevice = sensorDeviceList.get(sensorDeviceList.size() - 1);
        assertThat(testSensorDevice.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testSensorDevice.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingSensorDevice() throws Exception {
        int databaseSizeBeforeUpdate = sensorDeviceRepository.findAll().size();

        // Create the SensorDevice
        SensorDeviceDTO sensorDeviceDTO = sensorDeviceMapper.toDto(sensorDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restSensorDeviceMockMvc.perform(put("/api/sensor-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorDeviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SensorDevice in the database
        List<SensorDevice> sensorDeviceList = sensorDeviceRepository.findAll();
        assertThat(sensorDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSensorDevice() throws Exception {
        // Initialize the database
        sensorDeviceRepository.saveAndFlush(sensorDevice);

        int databaseSizeBeforeDelete = sensorDeviceRepository.findAll().size();

        // Get the sensorDevice
        restSensorDeviceMockMvc.perform(delete("/api/sensor-devices/{id}", sensorDevice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SensorDevice> sensorDeviceList = sensorDeviceRepository.findAll();
        assertThat(sensorDeviceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SensorDevice.class);
        SensorDevice sensorDevice1 = new SensorDevice();
        sensorDevice1.setId(1L);
        SensorDevice sensorDevice2 = new SensorDevice();
        sensorDevice2.setId(sensorDevice1.getId());
        assertThat(sensorDevice1).isEqualTo(sensorDevice2);
        sensorDevice2.setId(2L);
        assertThat(sensorDevice1).isNotEqualTo(sensorDevice2);
        sensorDevice1.setId(null);
        assertThat(sensorDevice1).isNotEqualTo(sensorDevice2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SensorDeviceDTO.class);
        SensorDeviceDTO sensorDeviceDTO1 = new SensorDeviceDTO();
        sensorDeviceDTO1.setId(1L);
        SensorDeviceDTO sensorDeviceDTO2 = new SensorDeviceDTO();
        assertThat(sensorDeviceDTO1).isNotEqualTo(sensorDeviceDTO2);
        sensorDeviceDTO2.setId(sensorDeviceDTO1.getId());
        assertThat(sensorDeviceDTO1).isEqualTo(sensorDeviceDTO2);
        sensorDeviceDTO2.setId(2L);
        assertThat(sensorDeviceDTO1).isNotEqualTo(sensorDeviceDTO2);
        sensorDeviceDTO1.setId(null);
        assertThat(sensorDeviceDTO1).isNotEqualTo(sensorDeviceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sensorDeviceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sensorDeviceMapper.fromId(null)).isNull();
    }
}
