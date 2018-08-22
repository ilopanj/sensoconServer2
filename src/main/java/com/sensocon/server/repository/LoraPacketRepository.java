package com.sensocon.server.repository;

import com.sensocon.server.domain.LoraPacket;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LoraPacket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoraPacketRepository extends JpaRepository<LoraPacket, Long> {

}
