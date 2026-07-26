package com.energymonitor.usageservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceEnergy {
    private String deviceId;
    private double energyConsumed;
    private String userId;
}
