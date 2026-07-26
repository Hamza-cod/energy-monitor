package com.energymonitor.deviceservice.service;

import com.energymonitor.deviceservice.dto.DeviceDto;
import com.energymonitor.deviceservice.entity.Device;
import com.energymonitor.deviceservice.exception.DeviceNotFoundException;
import com.energymonitor.deviceservice.mapper.DeviceMapper;
import com.energymonitor.deviceservice.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private DeviceMapper deviceMapper;
    @InjectMocks
    private DeviceService underTest;

    @Test
    void shouldGetDeviceById() {
        String id = "id";

        Device device = new Device();
        DeviceDto dto = new DeviceDto();

        when(deviceRepository.findById(id))
                .thenReturn(Optional.of(device));

        when(deviceMapper.toDeviceDto(device))
                .thenReturn(dto);

        DeviceDto result = underTest.getDeviceById(id);

        assertEquals(dto, result);

        verify(deviceRepository).findById(id);
        verify(deviceMapper).toDeviceDto(device);
    }

    @Test
    void shouldThrowExceptionWhenDeviceNotFound() {
        String id = "1";

        when(deviceRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                DeviceNotFoundException.class,
                () -> underTest.getDeviceById(id)
        );

        verify(deviceRepository).findById(id);
        verifyNoInteractions(deviceMapper);
    }

    @Test
    void shouldCreateDevice() {
    }

    @Test
    void updateDevice() {
    }

    @Test
    void deleteDevice() {
    }

    @Test
    void getAllDevicesByUserId() {
    }
}