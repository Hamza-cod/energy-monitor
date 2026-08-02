package com.energymonitor.insigthservice.http;

import com.energymonitor.common.dto.UsageDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(value = "/api/usage", version = "1.0")
public interface UsageClient {
    @GetExchange("/{userId}")
    UsageDto getXDaysUsageForUser(@PathVariable String userId, @RequestParam int days);
}
