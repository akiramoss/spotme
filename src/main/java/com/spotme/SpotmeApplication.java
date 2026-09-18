package com.spotme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point.
 * <p>
 * {@code @EnableJpaAuditing} activates the {@code createdAt}/{@code updatedAt}
 * auto-population defined in {@link com.spotme.common.AuditableEntity}.
 */
@SpringBootApplication
public class SpotmeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpotmeApplication.class, args);
    }
}
