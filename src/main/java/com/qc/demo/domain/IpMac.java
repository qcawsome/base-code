package com.qc.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A IpMac.
 */
@Entity
@Table(name = "ip_mac")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IpMac implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ip")
    private String ip;

    @Column(name = "mac")
    private String mac;

    @Column(name = "network_status")
    private String networkStatus;

    @Column(name = "agent_status")
    private String agentStatus;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ipMacs" }, allowSetters = true)
    private Device device;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IpMac id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return this.ip;
    }

    public IpMac ip(String ip) {
        this.setIp(ip);
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return this.mac;
    }

    public IpMac mac(String mac) {
        this.setMac(mac);
        return this;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNetworkStatus() {
        return this.networkStatus;
    }

    public IpMac networkStatus(String networkStatus) {
        this.setNetworkStatus(networkStatus);
        return this;
    }

    public void setNetworkStatus(String networkStatus) {
        this.networkStatus = networkStatus;
    }

    public String getAgentStatus() {
        return this.agentStatus;
    }

    public IpMac agentStatus(String agentStatus) {
        this.setAgentStatus(agentStatus);
        return this;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public IpMac device(Device device) {
        this.setDevice(device);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IpMac)) {
            return false;
        }
        return id != null && id.equals(((IpMac) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IpMac{" +
            "id=" + getId() +
            ", ip='" + getIp() + "'" +
            ", mac='" + getMac() + "'" +
            ", networkStatus='" + getNetworkStatus() + "'" +
            ", agentStatus='" + getAgentStatus() + "'" +
            "}";
    }
}
