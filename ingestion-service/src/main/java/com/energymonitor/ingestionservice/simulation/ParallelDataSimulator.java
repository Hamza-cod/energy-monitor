package com.energymonitor.ingestionservice.simulation;

import com.energymonitor.ingestionservice.dto.EnergyUsageDto;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.UUID.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParallelDataSimulator implements CommandLineRunner {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    @Value("${parallel.pool.size:9}")
    private int parallelPoolSize;
    @Value("${simulation.endpoint}")
    private String ingestionEndpoint;
    private int requestsPerInterval = 200;
    private final Random random = new Random();
    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public void run(String... args) throws Exception {
        ((ThreadPoolExecutor)executorService).setCorePoolSize(parallelPoolSize);
    }
    @Scheduled(fixedRateString = "${simulation.interval-ms}")
    public void sendMockData(){
        int batchSize = requestsPerInterval / parallelPoolSize;
        int remainder = requestsPerInterval % parallelPoolSize;

        for (int i = 0; i < parallelPoolSize; i++) {
            int requestsForThread = batchSize + (i < remainder ? 1 : 0);
            executorService.submit(() -> {
                for (int j = 0; j < requestsForThread; j++) {
                    EnergyUsageDto dto = EnergyUsageDto.builder()
                            .deviceId("ff266388-1512-41be-bcdc-c145db59d718")
                            .energyConsumed(Math.round(random.nextDouble(0.0, 2.0) * 100.0) / 100.0)
                            .timestamp(LocalDateTime.now()
                                    .atZone(ZoneId.systemDefault()).toInstant())
                            .build();
                    try {
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-api-v","1");
                        HttpEntity<EnergyUsageDto> request = new HttpEntity<>(dto, headers);
                        restTemplate.postForEntity(ingestionEndpoint, request, Void.class);
                        log.info("Sent mock data: " + dto);
                    } catch (Exception e) {
                        log.error("Failed to send data: " + e.getMessage());
                    }
                }
            });
        }
    }
    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        log.info("ParallelDataSimulator shut down.");
    }
}
