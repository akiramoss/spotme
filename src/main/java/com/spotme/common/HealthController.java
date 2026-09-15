package com.spotme.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/** Basic liveness check endpoint, used to verify the application is running. */
@RestController
public class HealthController {

    @GetMapping("/api/v1/health")
    public String health() {
        return "OK";
    }
}
