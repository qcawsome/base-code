package com.qc.demo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.qc.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IpMacTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IpMac.class);
        IpMac ipMac1 = new IpMac();
        ipMac1.setId(1L);
        IpMac ipMac2 = new IpMac();
        ipMac2.setId(ipMac1.getId());
        assertThat(ipMac1).isEqualTo(ipMac2);
        ipMac2.setId(2L);
        assertThat(ipMac1).isNotEqualTo(ipMac2);
        ipMac1.setId(null);
        assertThat(ipMac1).isNotEqualTo(ipMac2);
    }
}
