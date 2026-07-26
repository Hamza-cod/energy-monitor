package com.energymonitor.common.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.Instant;

/**
 * Request body accepted by ingestion-service's REST endpoint before it is
 * republished as an {@link EnergyUsageEvent}.
 *
 * <p>Kept distinct from the event so the public HTTP contract and the Kafka
 * contract can diverge; today they carry the same fields.
 */
@Builder
public record EnergyUsageDto(
        String deviceId,
        double energyConsumed,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant timestamp) {
}
