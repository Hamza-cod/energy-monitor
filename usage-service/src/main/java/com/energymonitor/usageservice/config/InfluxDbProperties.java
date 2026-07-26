package com.energymonitor.usageservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "influx-db")
public record InfluxDbProperties(String url, String token, String org, String bucket) {
}
