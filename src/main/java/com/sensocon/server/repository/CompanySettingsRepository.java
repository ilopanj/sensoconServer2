package com.sensocon.server.repository;

import com.sensocon.server.domain.CompanySettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CompanySettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanySettingsRepository extends JpaRepository<CompanySettings, Long> {

}
