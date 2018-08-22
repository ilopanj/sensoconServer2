package com.sensocon.server.service.impl;

import com.sensocon.server.service.SensorDeviceService;
import com.sensocon.server.domain.SensorDevice;
import com.sensocon.server.repository.SensorDeviceRepository;
import com.sensocon.server.service.dto.SensorDeviceDTO;
import com.sensocon.server.service.mapper.SensorDeviceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing SensorDevice.
 */
@Service
@Transactional
public class SensorDeviceServiceImpl implements SensorDeviceService {

    private final Logger log = LoggerFactory.getLogger(SensorDeviceServiceImpl.class);

    private final SensorDeviceRepository sensorDeviceRepository;

    private final SensorDeviceMapper sensorDeviceMapper;

    public SensorDeviceServiceImpl(SensorDeviceRepository sensorDeviceRepository, SensorDeviceMapper sensorDeviceMapper) {
        this.sensorDeviceRepository = sensorDeviceRepository;
        this.sensorDeviceMapper = sensorDeviceMapper;
    }

    /**
     * Save a sensorDevice.
     *
     * @param sensorDeviceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SensorDeviceDTO save(SensorDeviceDTO sensorDeviceDTO) {
        log.debug("Request to save SensorDevice : {}", sensorDeviceDTO);
        SensorDevice sensorDevice = sensorDeviceMapper.toEntity(sensorDeviceDTO);
        sensorDevice = sensorDeviceRepository.save(sensorDevice);
        return sensorDeviceMapper.toDto(sensorDevice);
    }

    /**
     * Get all the sensorDevices.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SensorDeviceDTO> findAll() {
        log.debug("Request to get all SensorDevices");
        return sensorDeviceRepository.findAll().stream()
            .map(sensorDeviceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one sensorDevice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SensorDeviceDTO> findOne(Long id) {
        log.debug("Request to get SensorDevice : {}", id);
        return sensorDeviceRepository.findById(id)
            .map(sensorDeviceMapper::toDto);
    }

    /**
     * Delete the sensorDevice by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SensorDevice : {}", id);
        sensorDeviceRepository.deleteById(id);
    }
}
