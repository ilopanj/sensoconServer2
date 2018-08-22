package com.sensocon.server.repository;

import com.sensocon.server.domain.LoraGateway;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LoraGateway entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoraGatewayRepository extends JpaRepository<LoraGateway, Long> {

}
