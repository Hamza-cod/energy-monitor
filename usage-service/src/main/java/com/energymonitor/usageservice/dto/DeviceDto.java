package com.energymonitor.usageservice.dto;

import lombok.Builder;

@Builder
public record DeviceDto(String id,
                        String name,
                        String type,
                        String location,
                        String userId,
                        Double energyConsumed) {
}
