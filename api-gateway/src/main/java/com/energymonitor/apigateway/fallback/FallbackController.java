package com.energymonitor.apigateway.fallback;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Terminal responses for requests whose downstream circuit breaker is open or
 * whose call timed out. Returns 503 with a Retry-After hint rather than letting
 * the caller see a raw connection error.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    /** Matches waitDurationInOpenState in the resilience4j config. */
    private static final String RETRY_AFTER_SECONDS = "5";

    @GetMapping(value = "/{service}", produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    public ResponseEntity<ProblemDetail> fallback(@PathVariable String service) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                "The %s is temporarily unavailable. Please retry shortly.".formatted(service.replace('-', ' ')));
        body.setTitle("Service Unavailable");
        body.setType(URI.create("https://energymonitor.com/problems/service-unavailable"));
        body.setProperty("service", service);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .header("Retry-After", RETRY_AFTER_SECONDS)
                .body(body);
    }
}
