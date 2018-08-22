package com.sensocon.server.service.impl;

import com.sensocon.server.service.CompanySettingsService;
import com.sensocon.server.domain.CompanySettings;
import com.sensocon.server.repository.CompanySettingsRepository;
import com.sensocon.server.service.dto.CompanySettingsDTO;
import com.sensocon.server.service.mapper.CompanySettingsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing CompanySettings.
 */
@Service
@Transactional
public class CompanySettingsServiceImpl implements CompanySettingsService {

    private final Logger log = LoggerFactory.getLogger(CompanySettingsServiceImpl.class);

    private final CompanySettingsRepository companySettingsRepository;

    private final CompanySettingsMapper companySettingsMapper;

    public CompanySettingsServiceImpl(CompanySettingsRepository companySettingsRepository, CompanySettingsMapper companySettingsMapper) {
        this.companySettingsRepository = companySettingsRepository;
        this.companySettingsMapper = companySettingsMapper;
    }

    /**
     * Save a companySettings.
     *
     * @param companySettingsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CompanySettingsDTO save(CompanySettingsDTO companySettingsDTO) {
        log.debug("Request to save CompanySettings : {}", companySettingsDTO);
        CompanySettings companySettings = companySettingsMapper.toEntity(companySettingsDTO);
        companySettings = companySettingsRepository.save(companySettings);
        return companySettingsMapper.toDto(companySettings);
    }

    /**
     * Get all the companySettings.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CompanySettingsDTO> findAll() {
        log.debug("Request to get all CompanySettings");
        return companySettingsRepository.findAll().stream()
            .map(companySettingsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one companySettings by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CompanySettingsDTO> findOne(Long id) {
        log.debug("Request to get CompanySettings : {}", id);
        return companySettingsRepository.findById(id)
            .map(companySettingsMapper::toDto);
    }

    /**
     * Delete the companySettings by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompanySettings : {}", id);
        companySettingsRepository.deleteById(id);
    }
}
