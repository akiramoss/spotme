package com.spotme.auth.service;

import com.spotme.auth.dto.AuthResponse;
import com.spotme.auth.dto.LoginRequest;
import com.spotme.auth.dto.RegisterRequest;
import com.spotme.auth.exception.InvalidCredentialsException;
import com.spotme.auth.exception.UserAlreadyExistsException;
import com.spotme.auth.security.JwtService;
import com.spotme.user.domain.User;
import com.spotme.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerSucceedsWhenEmailAndUsernameAreFree() {
        RegisterRequest request = new RegisterRequest("new@spotme.com", "newuser", "Passw0rd!");

        when(userRepository.findByEmail("new@spotme.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Passw0rd!")).thenReturn("hashed");
        when(jwtService.generateToken(any(User.class))).thenReturn("fake-jwt-token");

        AuthResponse response = authService.register(request);

        assertThat(response.token()).isEqualTo("fake-jwt-token");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerFailsWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest("taken@spotme.com", "newuser", "Passw0rd!");
        User existing = new User("taken@spotme.com", "someone", "hash");

        when(userRepository.findByEmail("taken@spotme.com")).thenReturn(Optional.of(existing));

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerFailsWhenUsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest("new@spotme.com", "takenuser", "Passw0rd!");
        User existing = new User("someone@spotme.com", "takenuser", "hash");

        when(userRepository.findByEmail("new@spotme.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("takenuser")).thenReturn(Optional.of(existing));

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void loginSucceedsWithCorrectCredentials() {
        LoginRequest request = new LoginRequest("test@spotme.com", "Passw0rd!");
        User user = new User("test@spotme.com", "testuser", "hashed_password");

        when(userRepository.findByEmail("test@spotme.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Passw0rd!", "hashed_password")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("fake-jwt-token");

        AuthResponse response = authService.login(request);

        assertThat(response.token()).isEqualTo("fake-jwt-token");
    }

    @Test
    void loginFailsWithNonExistentEmail() {
        LoginRequest request = new LoginRequest("noexiste@spotme.com", "Passw0rd!");

        when(userRepository.findByEmail("noexiste@spotme.com")).thenReturn(Optional.empty());

        InvalidCredentialsException ex = assertThrows(
                InvalidCredentialsException.class, () -> authService.login(request));
        assertThat(ex.getMessage()).isEqualTo("Invalid email or password");
    }

    @Test
    void loginFailsWithWrongPassword() {
        LoginRequest request = new LoginRequest("test@spotme.com", "WrongPassword!");
        User user = new User("test@spotme.com", "testuser", "hashed_password");

        when(userRepository.findByEmail("test@spotme.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("WrongPassword!", "hashed_password")).thenReturn(false);

        InvalidCredentialsException ex = assertThrows(
                InvalidCredentialsException.class, () -> authService.login(request));
        assertThat(ex.getMessage()).isEqualTo("Invalid email or password");
    }
}
