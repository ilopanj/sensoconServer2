package com.sensocon.server.service.impl;

import com.sensocon.server.service.LoraGatewayService;
import com.sensocon.server.domain.LoraGateway;
import com.sensocon.server.repository.LoraGatewayRepository;
import com.sensocon.server.service.dto.LoraGatewayDTO;
import com.sensocon.server.service.mapper.LoraGatewayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing LoraGateway.
 */
@Service
@Transactional
public class LoraGatewayServiceImpl implements LoraGatewayService {

    private final Logger log = LoggerFactory.getLogger(LoraGatewayServiceImpl.class);

    private final LoraGatewayRepository loraGatewayRepository;

    private final LoraGatewayMapper loraGatewayMapper;

    public LoraGatewayServiceImpl(LoraGatewayRepository loraGatewayRepository, LoraGatewayMapper loraGatewayMapper) {
        this.loraGatewayRepository = loraGatewayRepository;
        this.loraGatewayMapper = loraGatewayMapper;
    }

    /**
     * Save a loraGateway.
     *
     * @param loraGatewayDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LoraGatewayDTO save(LoraGatewayDTO loraGatewayDTO) {
        log.debug("Request to save LoraGateway : {}", loraGatewayDTO);
        LoraGateway loraGateway = loraGatewayMapper.toEntity(loraGatewayDTO);
        loraGateway = loraGatewayRepository.save(loraGateway);
        return loraGatewayMapper.toDto(loraGateway);
    }

    /**
     * Get all the loraGateways.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoraGatewayDTO> findAll() {
        log.debug("Request to get all LoraGateways");
        return loraGatewayRepository.findAll().stream()
            .map(loraGatewayMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one loraGateway by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LoraGatewayDTO> findOne(Long id) {
        log.debug("Request to get LoraGateway : {}", id);
        return loraGatewayRepository.findById(id)
            .map(loraGatewayMapper::toDto);
    }

    /**
     * Delete the loraGateway by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoraGateway : {}", id);
        loraGatewayRepository.deleteById(id);
    }
}
