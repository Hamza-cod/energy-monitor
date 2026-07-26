package com.energymonitor.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User representation exchanged over HTTP between user-service (producer) and
 * usage-service (consumer).
 *
 * <p>{@code id} is a {@code String} rather than a {@code UUID}: it is compared
 * against {@code Device.userId}, which is stored as a String, so the value was
 * already crossing this boundary in string form.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDto extends BaseUserDto {
    private String id;
}
