package com.energymonitor.usageservice.http;

import com.energymonitor.common.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

@Component
@RequiredArgsConstructor
public class ResilientUserClient {

    private final UserClient userClient;

    @Retryable(
            includes = ResourceAccessException.class,
            maxRetries = 3,
            delayString = "500ms",
            multiplier = 2.0
    )
    public UserDto getUserById(String id) {
        return userClient.getUserById(id);
    }
}
