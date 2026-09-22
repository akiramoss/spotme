package com.spotme.auth.exception;

/**
 * Thrown when login fails — the message is intentionally generic
 * (never reveals whether the email exists or the password was wrong,
 * to avoid leaking which emails are registered).
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
