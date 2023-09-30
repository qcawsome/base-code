package com.qc.demo.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.qc.demo.domain.PrimeNgTable} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrimeNgTableDTO implements Serializable {

    private Long id;

    private String text;

    private Integer number;

    private Double floatNumber;

    private LocalDate date;

    private ZonedDateTime zoneDate;

    @Lob
    private byte[] image;

    private String imageContentType;

    @Lob
    private String longText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getFloatNumber() {
        return floatNumber;
    }

    public void setFloatNumber(Double floatNumber) {
        this.floatNumber = floatNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ZonedDateTime getZoneDate() {
        return zoneDate;
    }

    public void setZoneDate(ZonedDateTime zoneDate) {
        this.zoneDate = zoneDate;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getLongText() {
        return longText;
    }

    public void setLongText(String longText) {
        this.longText = longText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrimeNgTableDTO)) {
            return false;
        }

        PrimeNgTableDTO primeNgTableDTO = (PrimeNgTableDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, primeNgTableDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrimeNgTableDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", number=" + getNumber() +
            ", floatNumber=" + getFloatNumber() +
            ", date='" + getDate() + "'" +
            ", zoneDate='" + getZoneDate() + "'" +
            ", image='" + getImage() + "'" +
            ", longText='" + getLongText() + "'" +
            "}";
    }
}
