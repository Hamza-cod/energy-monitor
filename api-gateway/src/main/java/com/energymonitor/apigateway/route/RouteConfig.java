package com.energymonitor.apigateway.route;

import com.energymonitor.apigateway.config.ServiceUris;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions.circuitBreaker;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.setPath;
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

    /**
     * Exposes each service's OpenAPI document through the gateway so the aggregated
     * Swagger UI can fetch them same-origin. {@code /v3/api-docs/user-service} is
     * rewritten to {@code /v3/api-docs} on the target, which is where springdoc
     * serves it. No circuit breaker here: a docs fetch failing is not worth tripping
     * the breaker that guards real API traffic.
     */
    @Bean
    public RouterFunction<ServerResponse> userApiDocsRoute() {
        return apiDocs("user-service", services.user());
    }

    @Bean
    public RouterFunction<ServerResponse> deviceApiDocsRoute() {
        return apiDocs("device-service", services.device());
    }

    @Bean
    public RouterFunction<ServerResponse> ingestionApiDocsRoute() {
        return apiDocs("ingestion-service", services.ingestion());
    }

    @Bean
    public RouterFunction<ServerResponse> usageApiDocsRoute() {
        return apiDocs("usage-service", services.usage());
    }

    @Bean
    public RouterFunction<ServerResponse> insightApiDocsRoute() {
        return apiDocs("insight-service", services.insight());
    }

    private static RouterFunction<ServerResponse> apiDocs(String id, String targetUri) {
        return route(id + "-api-docs")
                .route(RequestPredicates.path("/v3/api-docs/" + id), http())
                .before(uri(targetUri))
                .before(setPath("/v3/api-docs"))
                .build();
    }

    /**
     * Routes {@code path} to {@code targetUri} behind a circuit breaker named after
     * the route. When that breaker is open — or the call exceeds its timeout — the
     * request is forwarded to /fallback/{id} instead of surfacing the failure.
     */
    private static RouterFunction<ServerResponse> proxy(String id, String path, String targetUri) {
        return route(id)
                .route(RequestPredicates.path(path), http())
                .before(uri(targetUri))
                .filter(circuitBreaker(id, URI.create("forward:/fallback/" + id)))
                .build();
    }
}
