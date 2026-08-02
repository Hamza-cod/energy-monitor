package com.energymonitor.insigthservice.controller;

import com.energymonitor.insigthservice.dto.InsightDto;
import com.energymonitor.insigthservice.service.InsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/insight",version = "1")
@RequiredArgsConstructor
public class InsightController {

    private final InsightService insightService;


    @GetMapping("/saving-tips/{userId}")
    public ResponseEntity<InsightDto> getSavingTips(@PathVariable String userId) {
        final InsightDto insight = insightService.getSavingsTips(userId);
        return ResponseEntity.ok(insight);
    }

    @GetMapping("/overview/{userId}")
    public ResponseEntity<InsightDto> getOverview(@PathVariable String userId) {
        final InsightDto insight = insightService.getOverview(userId);
        return ResponseEntity.ok(insight);
    }


}
