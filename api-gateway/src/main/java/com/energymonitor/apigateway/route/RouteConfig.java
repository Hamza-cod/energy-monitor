package com.energymonitor.apigateway.route;

import com.energymonitor.apigateway.config.ServiceUris;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class RouteConfig {

    private final ServiceUris services;

    public RouteConfig(ServiceUris services) {
        this.services = services;
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return proxy("user-service", "/api/users/**", services.user());
    }

    @Bean
    public RouterFunction<ServerResponse> deviceServiceRoute() {
        return proxy("device-service", "/api/devices/**", services.device());
    }

    @Bean
    public RouterFunction<ServerResponse> ingestionServiceRoute() {
        return proxy("ingestion-service", "/api/ingestion/**", services.ingestion());
    }

    @Bean
    public RouterFunction<ServerResponse> usageServiceRoute() {
        return proxy("usage-service", "/api/usage/**", services.usage());
    }

    @Bean
    public RouterFunction<ServerResponse> insightServiceRoute() {
        return proxy("insight-service", "/api/insight/**", services.insight());
    }

    private static RouterFunction<ServerResponse> proxy(String id, String path, String targetUri) {
        return route(id)
                .route(RequestPredicates.path(path), http())
                .before(uri(targetUri))
                .build();
    }
}
