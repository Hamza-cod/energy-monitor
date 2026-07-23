package com.energymonitor.deviceservice.mapper;

import com.energymonitor.deviceservice.dto.DeviceDto;
import com.energymonitor.deviceservice.dto.request.DeviceCreateDto;
import com.energymonitor.deviceservice.dto.request.DeviceUpdateDto;
import com.energymonitor.deviceservice.entity.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    @Mapping(target = "id", ignore = true)
    Device toDevice(DeviceCreateDto deviceCreateDto);

    DeviceDto toDeviceDto(Device device);

    List<DeviceDto> toDeviceDtoList(List<Device> devices);

    @Mapping(target = "id", ignore = true)
    void updateDeviceFromDto(DeviceUpdateDto deviceUpdateDto, @MappingTarget Device device);
}
