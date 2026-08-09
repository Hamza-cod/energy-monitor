package com.energymonitor.insigthservice.http;

import com.energymonitor.common.dto.UsageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

@Component
@RequiredArgsConstructor
public class ResilientUsageClient {

    private final UsageClient usageClient;

    // Fewer retries than usage-service uses: this call still has the Ollama
    // request ahead of it inside the gateway's 30s budget for insight-service.
    @Retryable(
            includes = ResourceAccessException.class,
            maxRetries = 1,
            delayString = "500ms"
    )
    public UsageDto getXDaysUsageForUser(String userId, int days) {
        return usageClient.getXDaysUsageForUser(userId, days);
    }
}
