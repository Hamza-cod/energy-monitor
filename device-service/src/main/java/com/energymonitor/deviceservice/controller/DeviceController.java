package com.energymonitor.deviceservice.controller;

import com.energymonitor.deviceservice.dto.DeviceDto;
import com.energymonitor.deviceservice.dto.request.DeviceCreateDto;
import com.energymonitor.deviceservice.dto.request.DeviceUpdateDto;
import com.energymonitor.deviceservice.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/devices", version = "1")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable String id) {
        return ResponseEntity.ok(deviceService.getDeviceById(id));
    }

    @PostMapping
    public ResponseEntity<DeviceDto> createDevice(@Valid @RequestBody DeviceCreateDto request) {
        DeviceDto created = deviceService.createDevice(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceDto> updateDevice(@PathVariable String id,
                                                  @Valid @RequestBody DeviceUpdateDto request) {
        return ResponseEntity.ok(deviceService.updateDevice(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable String id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DeviceDto>> getAllDevicesByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(deviceService.getAllDevicesByUserId(userId));
    }
}
