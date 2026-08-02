package com.energymonitor.insigthservice.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ApiVersionInserter;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(group = "usage", types = UsageClient.class)
public class HttpConfig {

    @Value("${usage.client.base-url}")
    private String baseUrl;

    /**
     * Configures the "usage" group's client. A plain RestClient.Builder bean is not
     * picked up by @ImportHttpServices — each group builds its own client, so the
     * base URL and version inserter have to be applied through this configurer.
     */
    @Bean
    public RestClientHttpServiceGroupConfigurer usageGroupConfigurer() {
        return groups -> groups.filterByName("usage")
                .forEachClient((group, builder) -> builder
                        .baseUrl(baseUrl)
                        // Send the API version as the "x-api-v" header, matching
                        // usage-service's spring.mvc.apiversion.use.header config.
                        .apiVersionInserter(ApiVersionInserter.useHeader("x-api-v")));
    }
}
