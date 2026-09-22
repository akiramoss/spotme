package com.spotme.auth.service;

import com.spotme.auth.dto.AuthResponse;
import com.spotme.auth.dto.RegisterRequest;
import com.spotme.auth.exception.UserAlreadyExistsException;
import com.spotme.auth.security.JwtService;
import com.spotme.user.domain.User;
import com.spotme.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email already in use: " + request.email());
        }
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new UserAlreadyExistsException("Username already in use: " + request.username());
        }

        String passwordHash = passwordEncoder.encode(request.password());
        User user = new User(request.email(), request.username(), passwordHash);
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
