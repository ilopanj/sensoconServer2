package com.sensocon.server.service.mapper;

import com.sensocon.server.domain.*;
import com.sensocon.server.service.dto.SensorThresholdDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SensorThreshold and its DTO SensorThresholdDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SensorThresholdMapper extends EntityMapper<SensorThresholdDTO, SensorThreshold> {



    default SensorThreshold fromId(Long id) {
        if (id == null) {
            return null;
        }
        SensorThreshold sensorThreshold = new SensorThreshold();
        sensorThreshold.setId(id);
        return sensorThreshold;
    }
}
