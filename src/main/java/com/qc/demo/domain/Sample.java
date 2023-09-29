package com.qc.demo.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Sample.
 */
@Entity
@Table(name = "sample")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sample implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sample id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public Sample text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sample)) {
            return false;
        }
        return id != null && id.equals(((Sample) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sample{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            "}";
    }
}
