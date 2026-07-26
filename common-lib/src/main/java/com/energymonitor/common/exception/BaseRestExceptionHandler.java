package com.energymonitor.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.accept.InvalidApiVersionException;
import org.springframework.web.accept.MissingApiVersionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception handling shared by every web service: API versioning errors, bean
 * validation failures, and the common {@link BadRequestException} /
 * {@link ResourceNotFoundException} pair.
 *
 * <p>Deliberately <em>not</em> annotated with {@code @RestControllerAdvice} —
 * component scanning never reaches this package from the services, so each
 * service declares its own annotated subclass. That also lets a service add
 * handlers for its own exception types.
 */
public abstract class BaseRestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Resource not found");
        problem.setDetail(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(InvalidApiVersionException.class)
    public ResponseEntity<ProblemDetail> handleInvalidVersion(InvalidApiVersionException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Unsupported API version");
        problem.setDetail("API version '%s' is not supported.".formatted(ex.getVersion()));

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(MissingApiVersionException.class)
    public ResponseEntity<ProblemDetail> handleMissingVersion(MissingApiVersionException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Missing API version");
        problem.setDetail("The API version header is required.");

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(BadRequestException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }
}
