package com.energymonitor.usageservice.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ApiVersionInserter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(group = "device", types = DeviceClient.class)
public class HttpDeviceClientConfig {

    @Value("${device.client.base-url}")
    private String baseUrl;

    /**
     * Configures only the "device" group so the user client keeps its own base URL.
     */
    @Bean
    public RestClientHttpServiceGroupConfigurer deviceGroupConfigurer() {
        return groups -> groups.filterByName("device")
                .forEachClient((group, builder) -> builder
                        .baseUrl(baseUrl)
                        // Send the API version as the "x-api-v" header, matching
                        // device-service's spring.mvc.apiVersion.use.header config.
                        .apiVersionInserter(ApiVersionInserter.useHeader("x-api-v")));
    }
}