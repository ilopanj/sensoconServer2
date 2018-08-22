package com.sensocon.server.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the LoraGateway entity.
 */
public class LoraGatewayDTO implements Serializable {

    private Long id;

    private String gatewayId;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LoraGatewayDTO loraGatewayDTO = (LoraGatewayDTO) o;
        if (loraGatewayDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), loraGatewayDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LoraGatewayDTO{" +
            "id=" + getId() +
            ", gatewayId='" + getGatewayId() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
