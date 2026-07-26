package com.energymonitor.deviceservice.exception;

import com.energymonitor.common.exception.ResourceNotFoundException;

public class DeviceNotFoundException extends ResourceNotFoundException {

    private static final String MESSAGE_TEMPLATE = "Device not found with id %s";

    public DeviceNotFoundException(String id) {
        super(MESSAGE_TEMPLATE.formatted(id));
    }
}
