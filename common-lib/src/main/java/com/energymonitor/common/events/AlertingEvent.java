package com.energymonitor.common.events;

import lombok.Builder;

/**
 * Threshold-breach alert published by usage-service on {@code alert-usage-topic}.
 *
 * <p>See {@link EnergyUsageEvent} for why this type's package is significant to
 * Kafka deserialization.
 */
@Builder
public record AlertingEvent(
        String userId,
        Double threshold,
        Double energyConsumed,
        String email,
        String message) {
}
