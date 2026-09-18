package com.spotme.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Exposes the {@link PasswordEncoder} used to hash user passwords (BCrypt).
 * <p>
 * Kept separate from other Spring Security configuration (filter chain,
 * JWT) so this single bean can be reused independently — e.g. by a future
 * {@code UserService} that needs to hash a password without depending on
 * the full security filter setup.
 */
@Configuration
public class SecurityBeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
