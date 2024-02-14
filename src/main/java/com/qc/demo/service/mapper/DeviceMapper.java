package com.qc.demo.service.mapper;

import com.qc.demo.domain.Device;
import com.qc.demo.service.dto.DeviceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Device} and its DTO {@link DeviceDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeviceMapper extends EntityMapper<DeviceDTO, Device> {}
