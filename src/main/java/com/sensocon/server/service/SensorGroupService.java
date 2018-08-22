package com.sensocon.server.service;

import com.sensocon.server.service.dto.SensorGroupDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing SensorGroup.
 */
public interface SensorGroupService {

    /**
     * Save a sensorGroup.
     *
     * @param sensorGroupDTO the entity to save
     * @return the persisted entity
     */
    SensorGroupDTO save(SensorGroupDTO sensorGroupDTO);

    /**
     * Get all the sensorGroups.
     *
     * @return the list of entities
     */
    List<SensorGroupDTO> findAll();


    /**
     * Get the "id" sensorGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SensorGroupDTO> findOne(Long id);

    /**
     * Delete the "id" sensorGroup.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
