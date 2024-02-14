package com.qc.demo.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.qc.demo.domain.IpMac} entity. This class is used
 * in {@link com.qc.demo.web.rest.IpMacResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ip-macs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IpMacCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ip;

    private StringFilter mac;

    private StringFilter networkStatus;

    private StringFilter agentStatus;

    private LongFilter deviceId;

    private StringFilter deviceName;

    private Boolean distinct;

    public IpMacCriteria() {}

    public IpMacCriteria(IpMacCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ip = other.ip == null ? null : other.ip.copy();
        this.mac = other.mac == null ? null : other.mac.copy();
        this.networkStatus = other.networkStatus == null ? null : other.networkStatus.copy();
        this.agentStatus = other.agentStatus == null ? null : other.agentStatus.copy();
        this.deviceId = other.deviceId == null ? null : other.deviceId.copy();
        this.deviceName = other.deviceName == null ? null : other.deviceName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public IpMacCriteria copy() {
        return new IpMacCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getIp() {
        return ip;
    }

    public StringFilter ip() {
        if (ip == null) {
            ip = new StringFilter();
        }
        return ip;
    }

    public void setIp(StringFilter ip) {
        this.ip = ip;
    }

    public StringFilter getMac() {
        return mac;
    }

    public StringFilter mac() {
        if (mac == null) {
            mac = new StringFilter();
        }
        return mac;
    }

    public void setMac(StringFilter mac) {
        this.mac = mac;
    }

    public StringFilter getNetworkStatus() {
        return networkStatus;
    }

    public StringFilter networkStatus() {
        if (networkStatus == null) {
            networkStatus = new StringFilter();
        }
        return networkStatus;
    }

    public void setNetworkStatus(StringFilter networkStatus) {
        this.networkStatus = networkStatus;
    }

    public StringFilter getAgentStatus() {
        return agentStatus;
    }

    public StringFilter agentStatus() {
        if (agentStatus == null) {
            agentStatus = new StringFilter();
        }
        return agentStatus;
    }

    public void setAgentStatus(StringFilter agentStatus) {
        this.agentStatus = agentStatus;
    }

    public LongFilter getDeviceId() {
        return deviceId;
    }

    public LongFilter deviceId() {
        if (deviceId == null) {
            deviceId = new LongFilter();
        }
        return deviceId;
    }

    public void setDeviceId(LongFilter deviceId) {
        this.deviceId = deviceId;
    }

    public StringFilter getDeviceName() {
        return deviceName;
    }

    public StringFilter deviceName() {
        if (deviceName == null) {
            deviceName = new StringFilter();
        }
        return deviceName;
    }

    public void setDeviceName(StringFilter deviceName) {
        this.deviceName = deviceName;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IpMacCriteria that = (IpMacCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ip, that.ip) &&
            Objects.equals(mac, that.mac) &&
            Objects.equals(networkStatus, that.networkStatus) &&
            Objects.equals(agentStatus, that.agentStatus) &&
            Objects.equals(deviceId, that.deviceId) &&
            Objects.equals(deviceName, that.deviceName) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ip, mac, networkStatus, agentStatus, deviceId, deviceName, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IpMacCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (ip != null ? "ip=" + ip + ", " : "") +
            (mac != null ? "mac=" + mac + ", " : "") +
            (networkStatus != null ? "networkStatus=" + networkStatus + ", " : "") +
            (agentStatus != null ? "agentStatus=" + agentStatus + ", " : "") +
            (deviceId != null ? "deviceId=" + deviceId + ", " : "") +
            (deviceName != null ? "deviceName=" + deviceName + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
