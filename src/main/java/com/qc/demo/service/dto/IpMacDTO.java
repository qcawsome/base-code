package com.qc.demo.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.qc.demo.domain.IpMac} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IpMacDTO implements Serializable {

    private Long id;

    private String ip;

    private String mac;

    private String networkStatus;

    private String agentStatus;

    private DeviceDTO device;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(String networkStatus) {
        this.networkStatus = networkStatus;
    }

    public String getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    public DeviceDTO getDevice() {
        return device;
    }

    public void setDevice(DeviceDTO device) {
        this.device = device;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IpMacDTO)) {
            return false;
        }

        IpMacDTO ipMacDTO = (IpMacDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ipMacDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IpMacDTO{" +
            "id=" + getId() +
            ", ip='" + getIp() + "'" +
            ", mac='" + getMac() + "'" +
            ", networkStatus='" + getNetworkStatus() + "'" +
            ", agentStatus='" + getAgentStatus() + "'" +
            ", device=" + getDevice() +
            "}";
    }
}
