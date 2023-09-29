package com.qc.demo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.qc.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SampleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sample.class);
        Sample sample1 = new Sample();
        sample1.setId(1L);
        Sample sample2 = new Sample();
        sample2.setId(sample1.getId());
        assertThat(sample1).isEqualTo(sample2);
        sample2.setId(2L);
        assertThat(sample1).isNotEqualTo(sample2);
        sample1.setId(null);
        assertThat(sample1).isNotEqualTo(sample2);
    }
}
