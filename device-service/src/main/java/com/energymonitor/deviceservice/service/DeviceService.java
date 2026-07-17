package com.energymonitor.deviceservice.service;

import com.energymonitor.deviceservice.dto.DeviceDto;
import com.energymonitor.deviceservice.dto.request.DeviceCreateDto;
import com.energymonitor.deviceservice.dto.request.DeviceUpdateDto;
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

    public DeviceDto getDeviceById(Long id) {
        return deviceMapper.toDeviceDto(findDeviceOrThrow(id));
    }

    public DeviceDto createDevice(DeviceCreateDto request) {
        Device device = deviceMapper.toDevice(request);
        return deviceMapper.toDeviceDto(deviceRepository.save(device));
    }

    public DeviceDto updateDevice(Long id, DeviceUpdateDto request) {
        Device existing = findDeviceOrThrow(id);
        deviceMapper.updateDeviceFromDto(request, existing);
        return deviceMapper.toDeviceDto(deviceRepository.save(existing));
    }

    public void deleteDevice(Long id) {
        deviceRepository.delete(findDeviceOrThrow(id));
    }

    public List<DeviceDto> getAllDevicesByUserId(Long userId) {
        return deviceMapper.toDeviceDtoList(deviceRepository.findAllByUserId(userId));
    }

    private Device findDeviceOrThrow(Long id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
    }
}
