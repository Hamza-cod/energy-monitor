package com.energymonitor.ingestionservice.service;

import com.energymonitor.common.events.EnergyUsageDto;
import com.energymonitor.common.events.EnergyUsageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.energymonitor.common.events.StaticEventNames.ENERGY_USAGE_TOPIC;

@RequiredArgsConstructor
@Service
public class IngestionService {
    private final KafkaTemplate<String, EnergyUsageEvent> kafkaTemplate;

    public void ingest(EnergyUsageDto input){
        EnergyUsageEvent event = EnergyUsageEvent.builder()
                .deviceId(input.deviceId())
                .energyConsumed(input.energyConsumed())
                .timestamp(input.timestamp())
                .build();
        kafkaTemplate.send(ENERGY_USAGE_TOPIC, event);
    }
}
