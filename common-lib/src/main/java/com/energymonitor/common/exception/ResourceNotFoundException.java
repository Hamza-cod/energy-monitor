package com.energymonitor.common.exception;

/**
 * Signals a missing entity that should surface as HTTP 404.
 *
 * <p>Services subclass this to keep a domain-specific type and message while
 * reusing the shared handler, e.g. {@code DeviceNotFoundException}.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
