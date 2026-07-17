package com.energymonitor.user_service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDto extends BaseUserDto {
    private UUID id;
}
