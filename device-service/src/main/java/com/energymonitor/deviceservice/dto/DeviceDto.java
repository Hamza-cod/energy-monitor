package com.energymonitor.deviceservice.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceDto extends BaseDeviceDto {
    private String id;
}
