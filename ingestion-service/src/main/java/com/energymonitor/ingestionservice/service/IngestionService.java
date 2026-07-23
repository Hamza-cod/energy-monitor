package com.energymonitor.ingestionservice.service;

import com.energymonitor.ingestionservice.dto.EnergyUsageDto;
import com.energymonitor.ingestionservice.kafka.event.EnergyUsageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IngestionService {
    private final KafkaTemplate<String, EnergyUsageEvent> kafkaTemplate;

    private static final String ENERGY_USAGE_TOPIC = "energy-usage-topic";
    public void ingest(EnergyUsageDto input){
        EnergyUsageEvent event = EnergyUsageEvent.builder()
                .deviceId(input.deviceId())
                .energyConsumed(input.energyConsumed())
                .timestamp(input.timestamp())
                .build();
        kafkaTemplate.send(ENERGY_USAGE_TOPIC, event);
    }
}
