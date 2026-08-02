package com.energymonitor.usageservice.http;

import com.energymonitor.common.dto.DeviceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResilientDeviceClient {

    private final DeviceClient deviceClient;

    @Retryable(
            includes = ResourceAccessException.class,
            maxRetries = 3,
            delayString = "500ms",
            multiplier = 2.0
    )
    public DeviceDto getDeviceById(String id) {
        return deviceClient.getDeviceById(id);
    }

    @Retryable(
            includes = ResourceAccessException.class,
            maxRetries = 3,
            delayString = "500ms",
            multiplier = 2.0
    )
    public List<DeviceDto> getAllDevicesForUser(String userId) {
        return deviceClient.getAllDevicesForUser(userId);
    }
}
