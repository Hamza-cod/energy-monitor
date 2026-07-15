package com.energymonito.user_service.service;

import com.energymonito.user_service.UserMapper;
import com.energymonito.user_service.dto.UserDto;
import com.energymonito.user_service.dto.request.UserCreateDto;
import com.energymonito.user_service.exception.BadRequestionException;
import com.energymonito.user_service.model.User;
import com.energymonito.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto create(UserCreateDto request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestionException("user already existis");
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
