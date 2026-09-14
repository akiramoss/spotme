package com.spotme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpotmeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpotmeApplication.class, args);
    }
}
