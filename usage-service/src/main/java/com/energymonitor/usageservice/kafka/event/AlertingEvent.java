package com.energymonitor.usageservice.kafka.event;

import lombok.Builder;

@Builder
public record AlertingEvent(
        String userId,
        Double threshold,
        Double energyConsumed,
        String email,
        String message
) {
}
