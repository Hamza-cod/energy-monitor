package com.energymonitor.deviceservice.exception;

public class DeviceNotFoundException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Device not found with id %s";

    public DeviceNotFoundException(String id) {
        super(MESSAGE_TEMPLATE.formatted(id));
    }
}
