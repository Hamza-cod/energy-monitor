package com.energymonitor.user_service.service;

import com.energymonitor.user_service.UserMapper;
import com.energymonitor.common.dto.UserDto;
import com.energymonitor.common.dto.request.UserCreateDto;
import com.energymonitor.common.exception.BadRequestException;
import com.energymonitor.user_service.model.User;
import com.energymonitor.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto create(UserCreateDto request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestException("user already existis");
        }
        User user = userMapper.toUser(request);
        User saved = userRepository.save(user);
        return userMapper.toUserDto(saved);
    }

    public UserDto getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElse(null);
    }

    public void updateUser(UUID id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }
}
