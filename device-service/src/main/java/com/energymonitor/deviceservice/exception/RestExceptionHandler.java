package com.energymonitor.deviceservice.exception;

import com.energymonitor.common.exception.BaseRestExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Activates the shared handlers for device-service.
 *
 * <p>{@link DeviceNotFoundException} extends {@code ResourceNotFoundException},
 * so the inherited 404 handler already covers it.
 */
@RestControllerAdvice
public class RestExceptionHandler extends BaseRestExceptionHandler {
}
