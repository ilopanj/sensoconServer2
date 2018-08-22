package com.sensocon.server.service;

import com.sensocon.server.service.dto.LoraPacketDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing LoraPacket.
 */
public interface LoraPacketService {

    /**
     * Save a loraPacket.
     *
     * @param loraPacketDTO the entity to save
     * @return the persisted entity
     */
    LoraPacketDTO save(LoraPacketDTO loraPacketDTO);

    /**
     * Get all the loraPackets.
     *
     * @return the list of entities
     */
    List<LoraPacketDTO> findAll();


    /**
     * Get the "id" loraPacket.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LoraPacketDTO> findOne(Long id);

    /**
     * Delete the "id" loraPacket.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
