package com.spotme.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Payload for {@code POST /auth/register}.
 */
public record RegisterRequest(

        @NotBlank
        @Email
        String email,

        @NotBlank
        String username,

        @NotBlank
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).+$",
                message = "Password must contain letters, numbers, and at least one special character"
        )
        String password
) {
}
