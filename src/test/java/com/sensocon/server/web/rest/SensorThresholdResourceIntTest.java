package com.sensocon.server.web.rest;

import com.sensocon.server.SensoconServer2App;

import com.sensocon.server.domain.SensorThreshold;
import com.sensocon.server.repository.SensorThresholdRepository;
import com.sensocon.server.service.SensorThresholdService;
import com.sensocon.server.service.dto.SensorThresholdDTO;
import com.sensocon.server.service.mapper.SensorThresholdMapper;
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

import com.sensocon.server.domain.enumeration.ThresholdType;
/**
 * Test class for the SensorThresholdResource REST controller.
 *
 * @see SensorThresholdResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SensoconServer2App.class)
public class SensorThresholdResourceIntTest {

    private static final ThresholdType DEFAULT_TYPE = ThresholdType.THRESHOLD_GE;
    private static final ThresholdType UPDATED_TYPE = ThresholdType.THRESHOLD_LE;

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    @Autowired
    private SensorThresholdRepository sensorThresholdRepository;


    @Autowired
    private SensorThresholdMapper sensorThresholdMapper;
    

    @Autowired
    private SensorThresholdService sensorThresholdService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSensorThresholdMockMvc;

    private SensorThreshold sensorThreshold;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SensorThresholdResource sensorThresholdResource = new SensorThresholdResource(sensorThresholdService);
        this.restSensorThresholdMockMvc = MockMvcBuilders.standaloneSetup(sensorThresholdResource)
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
    public static SensorThreshold createEntity(EntityManager em) {
        SensorThreshold sensorThreshold = new SensorThreshold()
            .type(DEFAULT_TYPE)
            .value(DEFAULT_VALUE);
        return sensorThreshold;
    }

    @Before
    public void initTest() {
        sensorThreshold = createEntity(em);
    }

    @Test
    @Transactional
    public void createSensorThreshold() throws Exception {
        int databaseSizeBeforeCreate = sensorThresholdRepository.findAll().size();

        // Create the SensorThreshold
        SensorThresholdDTO sensorThresholdDTO = sensorThresholdMapper.toDto(sensorThreshold);
        restSensorThresholdMockMvc.perform(post("/api/sensor-thresholds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorThresholdDTO)))
            .andExpect(status().isCreated());

        // Validate the SensorThreshold in the database
        List<SensorThreshold> sensorThresholdList = sensorThresholdRepository.findAll();
        assertThat(sensorThresholdList).hasSize(databaseSizeBeforeCreate + 1);
        SensorThreshold testSensorThreshold = sensorThresholdList.get(sensorThresholdList.size() - 1);
        assertThat(testSensorThreshold.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSensorThreshold.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createSensorThresholdWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sensorThresholdRepository.findAll().size();

        // Create the SensorThreshold with an existing ID
        sensorThreshold.setId(1L);
        SensorThresholdDTO sensorThresholdDTO = sensorThresholdMapper.toDto(sensorThreshold);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensorThresholdMockMvc.perform(post("/api/sensor-thresholds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorThresholdDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SensorThreshold in the database
        List<SensorThreshold> sensorThresholdList = sensorThresholdRepository.findAll();
        assertThat(sensorThresholdList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSensorThresholds() throws Exception {
        // Initialize the database
        sensorThresholdRepository.saveAndFlush(sensorThreshold);

        // Get all the sensorThresholdList
        restSensorThresholdMockMvc.perform(get("/api/sensor-thresholds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensorThreshold.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }
    

    @Test
    @Transactional
    public void getSensorThreshold() throws Exception {
        // Initialize the database
        sensorThresholdRepository.saveAndFlush(sensorThreshold);

        // Get the sensorThreshold
        restSensorThresholdMockMvc.perform(get("/api/sensor-thresholds/{id}", sensorThreshold.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sensorThreshold.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingSensorThreshold() throws Exception {
        // Get the sensorThreshold
        restSensorThresholdMockMvc.perform(get("/api/sensor-thresholds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSensorThreshold() throws Exception {
        // Initialize the database
        sensorThresholdRepository.saveAndFlush(sensorThreshold);

        int databaseSizeBeforeUpdate = sensorThresholdRepository.findAll().size();

        // Update the sensorThreshold
        SensorThreshold updatedSensorThreshold = sensorThresholdRepository.findById(sensorThreshold.getId()).get();
        // Disconnect from session so that the updates on updatedSensorThreshold are not directly saved in db
        em.detach(updatedSensorThreshold);
        updatedSensorThreshold
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE);
        SensorThresholdDTO sensorThresholdDTO = sensorThresholdMapper.toDto(updatedSensorThreshold);

        restSensorThresholdMockMvc.perform(put("/api/sensor-thresholds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorThresholdDTO)))
            .andExpect(status().isOk());

        // Validate the SensorThreshold in the database
        List<SensorThreshold> sensorThresholdList = sensorThresholdRepository.findAll();
        assertThat(sensorThresholdList).hasSize(databaseSizeBeforeUpdate);
        SensorThreshold testSensorThreshold = sensorThresholdList.get(sensorThresholdList.size() - 1);
        assertThat(testSensorThreshold.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSensorThreshold.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingSensorThreshold() throws Exception {
        int databaseSizeBeforeUpdate = sensorThresholdRepository.findAll().size();

        // Create the SensorThreshold
        SensorThresholdDTO sensorThresholdDTO = sensorThresholdMapper.toDto(sensorThreshold);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restSensorThresholdMockMvc.perform(put("/api/sensor-thresholds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorThresholdDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SensorThreshold in the database
        List<SensorThreshold> sensorThresholdList = sensorThresholdRepository.findAll();
        assertThat(sensorThresholdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSensorThreshold() throws Exception {
        // Initialize the database
        sensorThresholdRepository.saveAndFlush(sensorThreshold);

        int databaseSizeBeforeDelete = sensorThresholdRepository.findAll().size();

        // Get the sensorThreshold
        restSensorThresholdMockMvc.perform(delete("/api/sensor-thresholds/{id}", sensorThreshold.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SensorThreshold> sensorThresholdList = sensorThresholdRepository.findAll();
        assertThat(sensorThresholdList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SensorThreshold.class);
        SensorThreshold sensorThreshold1 = new SensorThreshold();
        sensorThreshold1.setId(1L);
        SensorThreshold sensorThreshold2 = new SensorThreshold();
        sensorThreshold2.setId(sensorThreshold1.getId());
        assertThat(sensorThreshold1).isEqualTo(sensorThreshold2);
        sensorThreshold2.setId(2L);
        assertThat(sensorThreshold1).isNotEqualTo(sensorThreshold2);
        sensorThreshold1.setId(null);
        assertThat(sensorThreshold1).isNotEqualTo(sensorThreshold2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SensorThresholdDTO.class);
        SensorThresholdDTO sensorThresholdDTO1 = new SensorThresholdDTO();
        sensorThresholdDTO1.setId(1L);
        SensorThresholdDTO sensorThresholdDTO2 = new SensorThresholdDTO();
        assertThat(sensorThresholdDTO1).isNotEqualTo(sensorThresholdDTO2);
        sensorThresholdDTO2.setId(sensorThresholdDTO1.getId());
        assertThat(sensorThresholdDTO1).isEqualTo(sensorThresholdDTO2);
        sensorThresholdDTO2.setId(2L);
        assertThat(sensorThresholdDTO1).isNotEqualTo(sensorThresholdDTO2);
        sensorThresholdDTO1.setId(null);
        assertThat(sensorThresholdDTO1).isNotEqualTo(sensorThresholdDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sensorThresholdMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sensorThresholdMapper.fromId(null)).isNull();
    }
}
