package com.qc.demo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.qc.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SampleUuidDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SampleUuidDTO.class);
        SampleUuidDTO sampleUuidDTO1 = new SampleUuidDTO();
        sampleUuidDTO1.setId(1L);
        SampleUuidDTO sampleUuidDTO2 = new SampleUuidDTO();
        assertThat(sampleUuidDTO1).isNotEqualTo(sampleUuidDTO2);
        sampleUuidDTO2.setId(sampleUuidDTO1.getId());
        assertThat(sampleUuidDTO1).isEqualTo(sampleUuidDTO2);
        sampleUuidDTO2.setId(2L);
        assertThat(sampleUuidDTO1).isNotEqualTo(sampleUuidDTO2);
        sampleUuidDTO1.setId(null);
        assertThat(sampleUuidDTO1).isNotEqualTo(sampleUuidDTO2);
    }
}
