package com.energymonitor.ingestionservice.controller;

import com.energymonitor.ingestionservice.dto.EnergyUsageDto;
import com.energymonitor.ingestionservice.service.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/ingestion",version = "1")
@RequiredArgsConstructor
public class IngestionController {
    private final IngestionService ingestionService;

    @PostMapping
    public void ingest(@RequestBody EnergyUsageDto data) {
        ingestionService.ingest(data);
    }

}
