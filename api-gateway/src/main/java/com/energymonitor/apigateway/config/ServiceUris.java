package com.energymonitor.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Downstream service addresses, bound from the {@code services.*} block in
 * application.yaml so each environment can point the gateway elsewhere without
 * a rebuild.
 */
@ConfigurationProperties(prefix = "services")
public record ServiceUris(
        String user,
        String device,
        String ingestion,
        String usage,
        String insight
) {
}
