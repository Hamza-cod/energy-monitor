package com.energymonitor.common.exception;

/**
 * Signals a client error that should surface as HTTP 400.
 *
 * <p>Replaces user-service's {@code BadRequestionException}.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
