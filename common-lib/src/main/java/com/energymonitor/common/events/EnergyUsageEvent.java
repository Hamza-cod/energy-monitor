package com.energymonitor.common.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.Instant;

/**
 * Energy reading published by ingestion-service on {@code energy-usage-topic}
 * and consumed by usage-service.
 *
 * <p>The fully qualified name of this record is the {@code __TypeId__} header
 * value written by {@code JsonSerializer}, and it must stay within the package
 * allow-listed by {@code spring.json.trusted.packages}. Moving or renaming it
 * breaks deserialization on every consumer.
 */
@Builder
public record EnergyUsageEvent(
        String deviceId,
        double energyConsumed,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant timestamp) {
}
