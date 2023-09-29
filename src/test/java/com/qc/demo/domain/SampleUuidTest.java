package com.qc.demo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.qc.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SampleUuidTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SampleUuid.class);
        SampleUuid sampleUuid1 = new SampleUuid();
        sampleUuid1.setId(1L);
        SampleUuid sampleUuid2 = new SampleUuid();
        sampleUuid2.setId(sampleUuid1.getId());
        assertThat(sampleUuid1).isEqualTo(sampleUuid2);
        sampleUuid2.setId(2L);
        assertThat(sampleUuid1).isNotEqualTo(sampleUuid2);
        sampleUuid1.setId(null);
        assertThat(sampleUuid1).isNotEqualTo(sampleUuid2);
    }
}
