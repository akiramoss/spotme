package com.spotme.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Payload for {@code POST /auth/login}.
 */
public record LoginRequest(
        @NotBlank String email,
        @NotBlank String password
) {
}
