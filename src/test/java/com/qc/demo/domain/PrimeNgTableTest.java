package com.qc.demo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.qc.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrimeNgTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrimeNgTable.class);
        PrimeNgTable primeNgTable1 = new PrimeNgTable();
        primeNgTable1.setId(1L);
        PrimeNgTable primeNgTable2 = new PrimeNgTable();
        primeNgTable2.setId(primeNgTable1.getId());
        assertThat(primeNgTable1).isEqualTo(primeNgTable2);
        primeNgTable2.setId(2L);
        assertThat(primeNgTable1).isNotEqualTo(primeNgTable2);
        primeNgTable1.setId(null);
        assertThat(primeNgTable1).isNotEqualTo(primeNgTable2);
    }
}
