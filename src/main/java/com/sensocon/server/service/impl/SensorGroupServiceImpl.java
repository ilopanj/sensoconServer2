package com.sensocon.server.service.impl;

import com.sensocon.server.service.SensorGroupService;
import com.sensocon.server.domain.SensorGroup;
import com.sensocon.server.repository.SensorGroupRepository;
import com.sensocon.server.service.dto.SensorGroupDTO;
import com.sensocon.server.service.mapper.SensorGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing SensorGroup.
 */
@Service
@Transactional
public class SensorGroupServiceImpl implements SensorGroupService {

    private final Logger log = LoggerFactory.getLogger(SensorGroupServiceImpl.class);

    private final SensorGroupRepository sensorGroupRepository;

    private final SensorGroupMapper sensorGroupMapper;

    public SensorGroupServiceImpl(SensorGroupRepository sensorGroupRepository, SensorGroupMapper sensorGroupMapper) {
        this.sensorGroupRepository = sensorGroupRepository;
        this.sensorGroupMapper = sensorGroupMapper;
    }

    /**
     * Save a sensorGroup.
     *
     * @param sensorGroupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SensorGroupDTO save(SensorGroupDTO sensorGroupDTO) {
        log.debug("Request to save SensorGroup : {}", sensorGroupDTO);
        SensorGroup sensorGroup = sensorGroupMapper.toEntity(sensorGroupDTO);
        sensorGroup = sensorGroupRepository.save(sensorGroup);
        return sensorGroupMapper.toDto(sensorGroup);
    }

    /**
     * Get all the sensorGroups.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SensorGroupDTO> findAll() {
        log.debug("Request to get all SensorGroups");
        return sensorGroupRepository.findAll().stream()
            .map(sensorGroupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one sensorGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SensorGroupDTO> findOne(Long id) {
        log.debug("Request to get SensorGroup : {}", id);
        return sensorGroupRepository.findById(id)
            .map(sensorGroupMapper::toDto);
    }

    /**
     * Delete the sensorGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SensorGroup : {}", id);
        sensorGroupRepository.deleteById(id);
    }
}
