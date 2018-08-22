package com.sensocon.server.service.mapper;

import com.sensocon.server.domain.*;
import com.sensocon.server.service.dto.SensorDeviceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SensorDevice and its DTO SensorDeviceDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class, NotificationGroupMapper.class})
public interface SensorDeviceMapper extends EntityMapper<SensorDeviceDTO, SensorDevice> {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "notificationGroup.id", target = "notificationGroupId")
    SensorDeviceDTO toDto(SensorDevice sensorDevice);

    @Mapping(target = "sensors", ignore = true)
    @Mapping(target = "loraPackets", ignore = true)
    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "notificationGroupId", target = "notificationGroup")
    SensorDevice toEntity(SensorDeviceDTO sensorDeviceDTO);

    default SensorDevice fromId(Long id) {
        if (id == null) {
            return null;
        }
        SensorDevice sensorDevice = new SensorDevice();
        sensorDevice.setId(id);
        return sensorDevice;
    }
}
