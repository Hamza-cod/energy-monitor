package com.energymonito.user_service;

import com.energymonito.user_service.dto.UserDto;
import com.energymonito.user_service.dto.request.UserCreateDto;
import com.energymonito.user_service.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateDto userCreateDto);
    UserDto toUserDto(User user);
}
