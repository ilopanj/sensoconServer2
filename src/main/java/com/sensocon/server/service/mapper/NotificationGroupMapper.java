package com.sensocon.server.service.mapper;

import com.sensocon.server.domain.*;
import com.sensocon.server.service.dto.NotificationGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity NotificationGroup and its DTO NotificationGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {ContactMapper.class})
public interface NotificationGroupMapper extends EntityMapper<NotificationGroupDTO, NotificationGroup> {


    @Mapping(target = "sensorDevices", ignore = true)
    NotificationGroup toEntity(NotificationGroupDTO notificationGroupDTO);

    default NotificationGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        NotificationGroup notificationGroup = new NotificationGroup();
        notificationGroup.setId(id);
        return notificationGroup;
    }
}
