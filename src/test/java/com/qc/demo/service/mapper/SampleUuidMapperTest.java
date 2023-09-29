package com.qc.demo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SampleUuidMapperTest {

    private SampleUuidMapper sampleUuidMapper;

    @BeforeEach
    public void setUp() {
        sampleUuidMapper = new SampleUuidMapperImpl();
    }
}
