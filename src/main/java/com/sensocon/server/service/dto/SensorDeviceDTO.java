package com.sensocon.server.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SensorDevice entity.
 */
public class SensorDeviceDTO implements Serializable {

    private Long id;

    @NotNull
    private String deviceId;

    private String name;

    private Long locationId;

    private Long notificationGroupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getNotificationGroupId() {
        return notificationGroupId;
    }

    public void setNotificationGroupId(Long notificationGroupId) {
        this.notificationGroupId = notificationGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SensorDeviceDTO sensorDeviceDTO = (SensorDeviceDTO) o;
        if (sensorDeviceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sensorDeviceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SensorDeviceDTO{" +
            "id=" + getId() +
            ", deviceId='" + getDeviceId() + "'" +
            ", name='" + getName() + "'" +
            ", location=" + getLocationId() +
            ", notificationGroup=" + getNotificationGroupId() +
            "}";
    }
}
