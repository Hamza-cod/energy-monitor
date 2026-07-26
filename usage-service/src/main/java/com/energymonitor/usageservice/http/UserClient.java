package com.energymonitor.usageservice.http;

import com.energymonitor.usageservice.dto.UserDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(value = "/api/users",version = "1")
public interface UserClient {
    @GetExchange("/{id}")
    UserDto getUserById(@PathVariable String id);
}
