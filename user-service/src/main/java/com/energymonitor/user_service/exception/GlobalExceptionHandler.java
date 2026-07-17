package com.energymonitor.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.accept.InvalidApiVersionException;
import org.springframework.web.accept.MissingApiVersionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidApiVersionException.class)
    public ResponseEntity<ProblemDetail> handleInvalidVersion(
            InvalidApiVersionException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Unsupported API version");
        problem.setDetail(
                "API version '%s' is not supported.".formatted(ex.getVersion()));

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(MissingApiVersionException.class)
    public ResponseEntity<ProblemDetail> handleMissingVersion(
            MissingApiVersionException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Missing API version");
        problem.setDetail("The API version header is required.");

        return ResponseEntity.badRequest().body(problem);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(BadRequestionException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestionException(
            BadRequestionException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("message",ex.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }
}