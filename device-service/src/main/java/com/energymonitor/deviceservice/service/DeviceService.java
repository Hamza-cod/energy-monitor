package com.energymonitor.deviceservice.service;

import com.energymonitor.common.dto.DeviceDto;
import com.energymonitor.common.dto.request.DeviceCreateDto;
import com.energymonitor.common.dto.request.DeviceUpdateDto;
import com.energymonitor.deviceservice.entity.Device;
import com.energymonitor.deviceservice.exception.DeviceNotFoundException;
import com.energymonitor.deviceservice.mapper.DeviceMapper;
import com.energymonitor.deviceservice.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    public DeviceDto getDeviceById(String id) {
        return deviceMapper.toDeviceDto(findDeviceOrThrow(id));
    }

    public DeviceDto createDevice(DeviceCreateDto request) {
        Device device = deviceMapper.toDevice(request);
        return deviceMapper.toDeviceDto(deviceRepository.save(device));
    }

    public DeviceDto updateDevice(String id, DeviceUpdateDto request) {
        Device existing = findDeviceOrThrow(id);
        deviceMapper.updateDeviceFromDto(request, existing);
        return deviceMapper.toDeviceDto(deviceRepository.save(existing));
    }

    public void deleteDevice(String id) {
        deviceRepository.delete(findDeviceOrThrow(id));
    }

    public List<DeviceDto> getAllDevicesByUserId(String userId) {
        return deviceMapper.toDeviceDtoList(deviceRepository.findAllByUserId(userId));
    }

    private Device findDeviceOrThrow(String id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
    }
}
