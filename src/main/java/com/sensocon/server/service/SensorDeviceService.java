package com.sensocon.server.service;

import com.sensocon.server.service.dto.SensorDeviceDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing SensorDevice.
 */
public interface SensorDeviceService {

    /**
     * Save a sensorDevice.
     *
     * @param sensorDeviceDTO the entity to save
     * @return the persisted entity
     */
    SensorDeviceDTO save(SensorDeviceDTO sensorDeviceDTO);

    /**
     * Get all the sensorDevices.
     *
     * @return the list of entities
     */
    List<SensorDeviceDTO> findAll();


    /**
     * Get the "id" sensorDevice.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SensorDeviceDTO> findOne(Long id);

    /**
     * Delete the "id" sensorDevice.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
