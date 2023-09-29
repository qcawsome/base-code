package com.qc.demo.service.mapper;

import com.qc.demo.domain.SampleUuid;
import com.qc.demo.service.dto.SampleUuidDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SampleUuid} and its DTO {@link SampleUuidDTO}.
 */
@Mapper(componentModel = "spring")
public interface SampleUuidMapper extends EntityMapper<SampleUuidDTO, SampleUuid> {}
