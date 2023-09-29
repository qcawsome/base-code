package com.qc.demo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.qc.demo.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SampleUuidTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SampleUuid.class);
        SampleUuid sampleUuid1 = new SampleUuid();
        sampleUuid1.setId(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef001"));
        SampleUuid sampleUuid2 = new SampleUuid();
        sampleUuid2.setId(sampleUuid1.getId());
        assertThat(sampleUuid1).isEqualTo(sampleUuid2);
        sampleUuid2.setId(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef002"));
        assertThat(sampleUuid1).isNotEqualTo(sampleUuid2);
        sampleUuid1.setId(null);
        assertThat(sampleUuid1).isNotEqualTo(sampleUuid2);
    }
}
