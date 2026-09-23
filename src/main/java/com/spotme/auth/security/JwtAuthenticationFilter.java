package com.spotme.auth.security;

import com.spotme.user.domain.User;
import com.spotme.user.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * Reads the {@code Authorization: Bearer <token>} header on every request,
 * validates the JWT, and — if valid — authenticates the user for the
 * duration of that request (stateless; no session is created).
 * <p>
 * Any failure (missing header, invalid or expired token, user no longer
 * existing) simply leaves the request unauthenticated; it is
 * {@code anyRequest().authenticated()} in {@link com.spotme.common.SecurityFilterChainConfig}
 * that rejects it with 401 when the route requires authentication.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());
            authenticate(token);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(String token) {
        try {
            Long userId = jwtService.extractUserId(token);
            Optional<User> user = userRepository.findById(userId);

            user.ifPresent(u -> {
                var authentication = new UsernamePasswordAuthenticationToken(
                        u, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            });
        } catch (JwtException | IllegalArgumentException e) {
            // Invalid/expired token, or malformed subject — request stays unauthenticated.
        }
    }
}
