package com.energymonitor.usageservice.dto;

import lombok.Builder;

@Builder
public record UserDto(
        String id,
        String name,
        String surname,
        String email,
        String address,
        boolean alerting,
        double energyAlertingThreshold
) {
}
