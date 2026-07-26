package com.energymonitor.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Device representation returned by device-service and consumed by usage-service.
 *
 * <p>{@code type} and {@code energyConsumed} are read-only projections that the
 * {@code Device} entity does not currently persist, so they are declared here
 * rather than on {@link BaseDeviceDto} — create/update requests must not accept
 * fields that cannot be stored. Both stay null until device-service populates them.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceDto extends BaseDeviceDto {

    private String id;

    private String type;

    private Double energyConsumed;
}
