package com.energymonitor.usageservice.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class InfluxDbConfig {
    private final InfluxDbProperties properties;



    @Bean
    public   InfluxDBClient influxDBClient () {
        return InfluxDBClientFactory.
                create(properties.url(), properties.token().toCharArray(), properties.org(), properties.bucket());
    }
}
