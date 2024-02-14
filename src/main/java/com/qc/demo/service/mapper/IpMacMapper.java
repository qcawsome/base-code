package com.qc.demo.service.mapper;

import com.qc.demo.domain.Device;
import com.qc.demo.domain.IpMac;
import com.qc.demo.service.dto.DeviceDTO;
import com.qc.demo.service.dto.IpMacDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IpMac} and its DTO {@link IpMacDTO}.
 */
@Mapper(componentModel = "spring")
public interface IpMacMapper extends EntityMapper<IpMacDTO, IpMac> {
    @Mapping(target = "device", source = "device", qualifiedByName = "deviceName")
    IpMacDTO toDto(IpMac s);

    @Named("deviceName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DeviceDTO toDtoDeviceName(Device device);
}
