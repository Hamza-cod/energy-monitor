package com.energymonitor.user_service.exception;

import com.energymonitor.common.exception.BaseRestExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Activates the shared handlers for user-service. Add user-specific
 * {@code @ExceptionHandler} methods here.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends BaseRestExceptionHandler {
}
