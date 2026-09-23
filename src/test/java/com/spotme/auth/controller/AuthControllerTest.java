package com.spotme.auth.controller;

import tools.jackson.databind.ObjectMapper;
import com.spotme.auth.dto.AuthResponse;
import com.spotme.auth.dto.LoginRequest;
import com.spotme.auth.dto.RegisterRequest;
import com.spotme.auth.exception.InvalidCredentialsException;
import com.spotme.auth.exception.UserAlreadyExistsException;
import com.spotme.auth.service.AuthService;
import com.spotme.auth.security.JwtService;
import com.spotme.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void registerReturns201WithToken() throws Exception {
        RegisterRequest request = new RegisterRequest("new@spotme.com", "newuser", "Passw0rd!");
        when(authService.register(any())).thenReturn(new AuthResponse("fake-jwt-token"));

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void registerReturns400ForInvalidEmail() throws Exception {
        RegisterRequest request = new RegisterRequest("not-an-email", "newuser", "Passw0rd!");

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerReturns400ForWeakPassword() throws Exception {
        RegisterRequest request = new RegisterRequest("new@spotme.com", "newuser", "weak");

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerReturns409WhenUserAlreadyExists() throws Exception {
        RegisterRequest request = new RegisterRequest("taken@spotme.com", "newuser", "Passw0rd!");
        when(authService.register(any()))
                .thenThrow(new UserAlreadyExistsException("Email already in use: taken@spotme.com"));

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Email already in use: taken@spotme.com"));
    }

    @Test
    void loginReturns200WithToken() throws Exception {
        LoginRequest request = new LoginRequest("test@spotme.com", "Passw0rd!");
        when(authService.login(any())).thenReturn(new AuthResponse("fake-jwt-token"));

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void loginReturns401ForInvalidCredentials() throws Exception {
        LoginRequest request = new LoginRequest("test@spotme.com", "WrongPassword!");
        when(authService.login(any()))
                .thenThrow(new InvalidCredentialsException("Invalid email or password"));

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));
    }
}
