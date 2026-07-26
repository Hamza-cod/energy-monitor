package com.energymonitor.usageservice.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ApiVersionInserter;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(group = "user", types = UserClient.class)
public class HttpUserClientConfig {

    @Value("${user.client.base-url}")
    private String baseUrl;

    /**
     * Configures only the "user" group so the device client keeps its own base URL.
     */
    @Bean
    public RestClientHttpServiceGroupConfigurer userGroupConfigurer() {
        return groups -> groups.filterByName("user")
                .forEachClient((group, builder) -> builder
                        .baseUrl(baseUrl)
                        .apiVersionInserter(ApiVersionInserter.useHeader("x-api-v")));
    }
}