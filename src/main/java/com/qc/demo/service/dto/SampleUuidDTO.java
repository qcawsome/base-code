package com.qc.demo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.qc.demo.domain.SampleUuid} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SampleUuidDTO implements Serializable {

    private Long id;

    private UUID uuid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SampleUuidDTO)) {
            return false;
        }

        SampleUuidDTO sampleUuidDTO = (SampleUuidDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sampleUuidDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SampleUuidDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            "}";
    }
}
