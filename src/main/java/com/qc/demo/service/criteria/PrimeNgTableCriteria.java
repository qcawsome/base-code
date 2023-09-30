package com.qc.demo.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.qc.demo.domain.PrimeNgTable} entity. This class is used
 * in {@link com.qc.demo.web.rest.PrimeNgTableResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /prime-ng-tables?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrimeNgTableCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter text;

    private IntegerFilter number;

    private DoubleFilter floatNumber;

    private LocalDateFilter date;

    private ZonedDateTimeFilter zoneDate;

    private Boolean distinct;

    public PrimeNgTableCriteria() {}

    public PrimeNgTableCriteria(PrimeNgTableCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.text = other.text == null ? null : other.text.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.floatNumber = other.floatNumber == null ? null : other.floatNumber.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.zoneDate = other.zoneDate == null ? null : other.zoneDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PrimeNgTableCriteria copy() {
        return new PrimeNgTableCriteria(this);
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

    public StringFilter getText() {
        return text;
    }

    public StringFilter text() {
        if (text == null) {
            text = new StringFilter();
        }
        return text;
    }

    public void setText(StringFilter text) {
        this.text = text;
    }

    public IntegerFilter getNumber() {
        return number;
    }

    public IntegerFilter number() {
        if (number == null) {
            number = new IntegerFilter();
        }
        return number;
    }

    public void setNumber(IntegerFilter number) {
        this.number = number;
    }

    public DoubleFilter getFloatNumber() {
        return floatNumber;
    }

    public DoubleFilter floatNumber() {
        if (floatNumber == null) {
            floatNumber = new DoubleFilter();
        }
        return floatNumber;
    }

    public void setFloatNumber(DoubleFilter floatNumber) {
        this.floatNumber = floatNumber;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public ZonedDateTimeFilter getZoneDate() {
        return zoneDate;
    }

    public ZonedDateTimeFilter zoneDate() {
        if (zoneDate == null) {
            zoneDate = new ZonedDateTimeFilter();
        }
        return zoneDate;
    }

    public void setZoneDate(ZonedDateTimeFilter zoneDate) {
        this.zoneDate = zoneDate;
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
        final PrimeNgTableCriteria that = (PrimeNgTableCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(text, that.text) &&
            Objects.equals(number, that.number) &&
            Objects.equals(floatNumber, that.floatNumber) &&
            Objects.equals(date, that.date) &&
            Objects.equals(zoneDate, that.zoneDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, number, floatNumber, date, zoneDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrimeNgTableCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (text != null ? "text=" + text + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (floatNumber != null ? "floatNumber=" + floatNumber + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (zoneDate != null ? "zoneDate=" + zoneDate + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
