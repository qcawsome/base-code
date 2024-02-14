package com.qc.demo.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.qc.demo.domain.Device} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceDTO implements Serializable {

    private Long id;

    private String name;

    private String agentStatus;

    private String networkStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    public String getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(String networkStatus) {
        this.networkStatus = networkStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceDTO)) {
            return false;
        }

        DeviceDTO deviceDTO = (DeviceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", agentStatus='" + getAgentStatus() + "'" +
            ", networkStatus='" + getNetworkStatus() + "'" +
            "}";
    }
}
