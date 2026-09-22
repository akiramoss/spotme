package com.spotme.auth.exception;

/**
 * Thrown when registration is attempted with an email or username
 * that already belongs to an existing user.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
