package com.sensocon.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SensorDevice.
 */
@Entity
@Table(name = "sensor_device")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SensorDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "sensorDevice")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Sensor> sensors = new HashSet<>();

    @OneToMany(mappedBy = "sensorDevice")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LoraPacket> loraPackets = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("sensorDevices")
    private Location location;

    @ManyToOne
    @JsonIgnoreProperties("sensorDevices")
    private NotificationGroup notificationGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public SensorDevice deviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public SensorDevice name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public SensorDevice sensors(Set<Sensor> sensors) {
        this.sensors = sensors;
        return this;
    }

    public SensorDevice addSensor(Sensor sensor) {
        this.sensors.add(sensor);
        sensor.setSensorDevice(this);
        return this;
    }

    public SensorDevice removeSensor(Sensor sensor) {
        this.sensors.remove(sensor);
        sensor.setSensorDevice(null);
        return this;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }

    public Set<LoraPacket> getLoraPackets() {
        return loraPackets;
    }

    public SensorDevice loraPackets(Set<LoraPacket> loraPackets) {
        this.loraPackets = loraPackets;
        return this;
    }

    public SensorDevice addLoraPacket(LoraPacket loraPacket) {
        this.loraPackets.add(loraPacket);
        loraPacket.setSensorDevice(this);
        return this;
    }

    public SensorDevice removeLoraPacket(LoraPacket loraPacket) {
        this.loraPackets.remove(loraPacket);
        loraPacket.setSensorDevice(null);
        return this;
    }

    public void setLoraPackets(Set<LoraPacket> loraPackets) {
        this.loraPackets = loraPackets;
    }

    public Location getLocation() {
        return location;
    }

    public SensorDevice location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public NotificationGroup getNotificationGroup() {
        return notificationGroup;
    }

    public SensorDevice notificationGroup(NotificationGroup notificationGroup) {
        this.notificationGroup = notificationGroup;
        return this;
    }

    public void setNotificationGroup(NotificationGroup notificationGroup) {
        this.notificationGroup = notificationGroup;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SensorDevice sensorDevice = (SensorDevice) o;
        if (sensorDevice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sensorDevice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SensorDevice{" +
            "id=" + getId() +
            ", deviceId='" + getDeviceId() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
