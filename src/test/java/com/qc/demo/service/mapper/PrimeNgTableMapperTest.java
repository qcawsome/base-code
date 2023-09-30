package com.qc.demo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrimeNgTableMapperTest {

    private PrimeNgTableMapper primeNgTableMapper;

    @BeforeEach
    public void setUp() {
        primeNgTableMapper = new PrimeNgTableMapperImpl();
    }
}
