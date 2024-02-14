package com.qc.demo.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.qc.demo.domain.Device} entity. This class is used
 * in {@link com.qc.demo.web.rest.DeviceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /devices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter agentStatus;

    private StringFilter networkStatus;

    private LongFilter ipMacsId;

    private Boolean distinct;

    public DeviceCriteria() {}

    public DeviceCriteria(DeviceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.agentStatus = other.agentStatus == null ? null : other.agentStatus.copy();
        this.networkStatus = other.networkStatus == null ? null : other.networkStatus.copy();
        this.ipMacsId = other.ipMacsId == null ? null : other.ipMacsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DeviceCriteria copy() {
        return new DeviceCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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

    public LongFilter getIpMacsId() {
        return ipMacsId;
    }

    public LongFilter ipMacsId() {
        if (ipMacsId == null) {
            ipMacsId = new LongFilter();
        }
        return ipMacsId;
    }

    public void setIpMacsId(LongFilter ipMacsId) {
        this.ipMacsId = ipMacsId;
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
        final DeviceCriteria that = (DeviceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(agentStatus, that.agentStatus) &&
            Objects.equals(networkStatus, that.networkStatus) &&
            Objects.equals(ipMacsId, that.ipMacsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, agentStatus, networkStatus, ipMacsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (agentStatus != null ? "agentStatus=" + agentStatus + ", " : "") +
            (networkStatus != null ? "networkStatus=" + networkStatus + ", " : "") +
            (ipMacsId != null ? "ipMacsId=" + ipMacsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
