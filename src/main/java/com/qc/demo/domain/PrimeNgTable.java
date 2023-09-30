package com.qc.demo.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A PrimeNgTable.
 */
@Entity
@Table(name = "prime_ng_table")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrimeNgTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "number")
    private Integer number;

    @Column(name = "float_number")
    private Double floatNumber;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "zone_date")
    private ZonedDateTime zoneDate;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Lob
    @Column(name = "long_text")
    private String longText;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PrimeNgTable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public PrimeNgTable text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getNumber() {
        return this.number;
    }

    public PrimeNgTable number(Integer number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getFloatNumber() {
        return this.floatNumber;
    }

    public PrimeNgTable floatNumber(Double floatNumber) {
        this.setFloatNumber(floatNumber);
        return this;
    }

    public void setFloatNumber(Double floatNumber) {
        this.floatNumber = floatNumber;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public PrimeNgTable date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ZonedDateTime getZoneDate() {
        return this.zoneDate;
    }

    public PrimeNgTable zoneDate(ZonedDateTime zoneDate) {
        this.setZoneDate(zoneDate);
        return this;
    }

    public void setZoneDate(ZonedDateTime zoneDate) {
        this.zoneDate = zoneDate;
    }

    public byte[] getImage() {
        return this.image;
    }

    public PrimeNgTable image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public PrimeNgTable imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getLongText() {
        return this.longText;
    }

    public PrimeNgTable longText(String longText) {
        this.setLongText(longText);
        return this;
    }

    public void setLongText(String longText) {
        this.longText = longText;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrimeNgTable)) {
            return false;
        }
        return id != null && id.equals(((PrimeNgTable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrimeNgTable{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", number=" + getNumber() +
            ", floatNumber=" + getFloatNumber() +
            ", date='" + getDate() + "'" +
            ", zoneDate='" + getZoneDate() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", longText='" + getLongText() + "'" +
            "}";
    }
}
