package com.qc.demo.service.mapper;

import com.qc.demo.domain.PrimeNgTable;
import com.qc.demo.service.dto.PrimeNgTableDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrimeNgTable} and its DTO {@link PrimeNgTableDTO}.
 */
@Mapper(componentModel = "spring")
public interface PrimeNgTableMapper extends EntityMapper<PrimeNgTableDTO, PrimeNgTable> {}
