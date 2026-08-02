package com.energymonitor.insigthservice.dto;

import lombok.Builder;

@Builder
public record InsightDto(
    String userId,
    String tips,
    double energyUsage
) {
}
