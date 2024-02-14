package com.qc.demo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.qc.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IpMacDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IpMacDTO.class);
        IpMacDTO ipMacDTO1 = new IpMacDTO();
        ipMacDTO1.setId(1L);
        IpMacDTO ipMacDTO2 = new IpMacDTO();
        assertThat(ipMacDTO1).isNotEqualTo(ipMacDTO2);
        ipMacDTO2.setId(ipMacDTO1.getId());
        assertThat(ipMacDTO1).isEqualTo(ipMacDTO2);
        ipMacDTO2.setId(2L);
        assertThat(ipMacDTO1).isNotEqualTo(ipMacDTO2);
        ipMacDTO1.setId(null);
        assertThat(ipMacDTO1).isNotEqualTo(ipMacDTO2);
    }
}
