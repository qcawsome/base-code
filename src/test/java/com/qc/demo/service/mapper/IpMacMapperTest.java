package com.qc.demo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IpMacMapperTest {

    private IpMacMapper ipMacMapper;

    @BeforeEach
    public void setUp() {
        ipMacMapper = new IpMacMapperImpl();
    }
}
