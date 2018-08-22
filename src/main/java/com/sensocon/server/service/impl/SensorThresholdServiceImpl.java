package com.sensocon.server.service.impl;

import com.sensocon.server.service.SensorThresholdService;
import com.sensocon.server.domain.SensorThreshold;
import com.sensocon.server.repository.SensorThresholdRepository;
import com.sensocon.server.service.dto.SensorThresholdDTO;
import com.sensocon.server.service.mapper.SensorThresholdMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing SensorThreshold.
 */
@Service
@Transactional
public class SensorThresholdServiceImpl implements SensorThresholdService {

    private final Logger log = LoggerFactory.getLogger(SensorThresholdServiceImpl.class);

    private final SensorThresholdRepository sensorThresholdRepository;

    private final SensorThresholdMapper sensorThresholdMapper;

    public SensorThresholdServiceImpl(SensorThresholdRepository sensorThresholdRepository, SensorThresholdMapper sensorThresholdMapper) {
        this.sensorThresholdRepository = sensorThresholdRepository;
        this.sensorThresholdMapper = sensorThresholdMapper;
    }

    /**
     * Save a sensorThreshold.
     *
     * @param sensorThresholdDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SensorThresholdDTO save(SensorThresholdDTO sensorThresholdDTO) {
        log.debug("Request to save SensorThreshold : {}", sensorThresholdDTO);
        SensorThreshold sensorThreshold = sensorThresholdMapper.toEntity(sensorThresholdDTO);
        sensorThreshold = sensorThresholdRepository.save(sensorThreshold);
        return sensorThresholdMapper.toDto(sensorThreshold);
    }

    /**
     * Get all the sensorThresholds.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SensorThresholdDTO> findAll() {
        log.debug("Request to get all SensorThresholds");
        return sensorThresholdRepository.findAll().stream()
            .map(sensorThresholdMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one sensorThreshold by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SensorThresholdDTO> findOne(Long id) {
        log.debug("Request to get SensorThreshold : {}", id);
        return sensorThresholdRepository.findById(id)
            .map(sensorThresholdMapper::toDto);
    }

    /**
     * Delete the sensorThreshold by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SensorThreshold : {}", id);
        sensorThresholdRepository.deleteById(id);
    }
}
