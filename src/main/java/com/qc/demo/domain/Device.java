package com.qc.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Device.
 */
@Entity
@Table(name = "device")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "agent_status")
    private String agentStatus;

    @Column(name = "network_status")
    private String networkStatus;

    @OneToMany(mappedBy = "device")
    @JsonIgnoreProperties(value = { "device" }, allowSetters = true)
    private Set<IpMac> ipMacs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Device id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Device name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgentStatus() {
        return this.agentStatus;
    }

    public Device agentStatus(String agentStatus) {
        this.setAgentStatus(agentStatus);
        return this;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    public String getNetworkStatus() {
        return this.networkStatus;
    }

    public Device networkStatus(String networkStatus) {
        this.setNetworkStatus(networkStatus);
        return this;
    }

    public void setNetworkStatus(String networkStatus) {
        this.networkStatus = networkStatus;
    }

    public Set<IpMac> getIpMacs() {
        return this.ipMacs;
    }

    public void setIpMacs(Set<IpMac> ipMacs) {
        if (this.ipMacs != null) {
            this.ipMacs.forEach(i -> i.setDevice(null));
        }
        if (ipMacs != null) {
            ipMacs.forEach(i -> i.setDevice(this));
        }
        this.ipMacs = ipMacs;
    }

    public Device ipMacs(Set<IpMac> ipMacs) {
        this.setIpMacs(ipMacs);
        return this;
    }

    public Device addIpMacs(IpMac ipMac) {
        this.ipMacs.add(ipMac);
        ipMac.setDevice(this);
        return this;
    }

    public Device removeIpMacs(IpMac ipMac) {
        this.ipMacs.remove(ipMac);
        ipMac.setDevice(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Device)) {
            return false;
        }
        return id != null && id.equals(((Device) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Device{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", agentStatus='" + getAgentStatus() + "'" +
            ", networkStatus='" + getNetworkStatus() + "'" +
            "}";
    }
}
