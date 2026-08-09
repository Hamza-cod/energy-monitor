package com.energymonitor.common.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UsageDto(
        String userId,
        List<DeviceDto> devices
) {
}
