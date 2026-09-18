package com.spotme.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures HTTP authorization rules.
 * <p>
 * {@code /auth/**} and the health check are public; everything else
 * requires authentication. Sessions are stateless — this API is
 * authenticated per-request via JWT (added separately once the token
 * utility exists), not via server-side session state.
 */
@Configuration
public class SecurityFilterChainConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/api/v1/health").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}
