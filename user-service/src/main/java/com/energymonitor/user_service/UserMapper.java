package com.energymonitor.user_service;

import com.energymonitor.common.dto.UserDto;
import com.energymonitor.common.dto.request.UserCreateDto;
import com.energymonitor.user_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreateDto userCreateDto);

    /** The entity is keyed by UUID; the shared DTO exposes it as a String. */
    @Mapping(target = "id", expression = "java(user.getId() == null ? null : user.getId().toString())")
    UserDto toUserDto(User user);
}
