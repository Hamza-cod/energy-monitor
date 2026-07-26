package com.energymonitor.usageservice.http;

import com.energymonitor.usageservice.dto.DeviceDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/api/devices")
public interface DeviceClient {


    @GetExchange(value = "/{id}", version = "1")
    DeviceDto getDeviceById(@PathVariable String id);
}
