package com.sensocon.server.repository;

import com.sensocon.server.domain.NotificationGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the NotificationGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationGroupRepository extends JpaRepository<NotificationGroup, Long> {

    @Query(value = "select distinct notification_group from NotificationGroup notification_group left join fetch notification_group.contacts",
        countQuery = "select count(distinct notification_group) from NotificationGroup notification_group")
    Page<NotificationGroup> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct notification_group from NotificationGroup notification_group left join fetch notification_group.contacts")
    List<NotificationGroup> findAllWithEagerRelationships();

    @Query("select notification_group from NotificationGroup notification_group left join fetch notification_group.contacts where notification_group.id =:id")
    Optional<NotificationGroup> findOneWithEagerRelationships(@Param("id") Long id);

}
