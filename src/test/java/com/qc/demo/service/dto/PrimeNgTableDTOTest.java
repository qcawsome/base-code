package com.qc.demo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.qc.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrimeNgTableDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrimeNgTableDTO.class);
        PrimeNgTableDTO primeNgTableDTO1 = new PrimeNgTableDTO();
        primeNgTableDTO1.setId(1L);
        PrimeNgTableDTO primeNgTableDTO2 = new PrimeNgTableDTO();
        assertThat(primeNgTableDTO1).isNotEqualTo(primeNgTableDTO2);
        primeNgTableDTO2.setId(primeNgTableDTO1.getId());
        assertThat(primeNgTableDTO1).isEqualTo(primeNgTableDTO2);
        primeNgTableDTO2.setId(2L);
        assertThat(primeNgTableDTO1).isNotEqualTo(primeNgTableDTO2);
        primeNgTableDTO1.setId(null);
        assertThat(primeNgTableDTO1).isNotEqualTo(primeNgTableDTO2);
    }
}
