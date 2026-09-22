package com.spotme.auth.dto;

/**
 * Response for successful registration or login — carries the issued JWT.
 */
public record AuthResponse(String token) {
}
