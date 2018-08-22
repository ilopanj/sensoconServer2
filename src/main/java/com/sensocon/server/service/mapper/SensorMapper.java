package com.sensocon.server.service.mapper;

import com.sensocon.server.domain.*;
import com.sensocon.server.service.dto.SensorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Sensor and its DTO SensorDTO.
 */
@Mapper(componentModel = "spring", uses = {SensorDeviceMapper.class})
public interface SensorMapper extends EntityMapper<SensorDTO, Sensor> {

    @Mapping(source = "sensorDevice.id", target = "sensorDeviceId")
    SensorDTO toDto(Sensor sensor);

    @Mapping(source = "sensorDeviceId", target = "sensorDevice")
    Sensor toEntity(SensorDTO sensorDTO);

    default Sensor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sensor sensor = new Sensor();
        sensor.setId(id);
        return sensor;
    }
}
