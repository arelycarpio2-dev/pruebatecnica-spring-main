package com.prueba.backend.service;

import com.prueba.backend.config.JwtUtil;
import com.prueba.backend.dto.LoginRequest;
import com.prueba.backend.dto.LoginResponse;
import com.prueba.backend.entity.UserEntity;
import com.prueba.backend.exception.InvalidCredentialsException;
import com.prueba.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void loginSuccess() {
        String encodedPassword = passwordEncoder.encode("admin");
        UserEntity user = new UserEntity("admin", encodedPassword);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("admin")).thenReturn("fake-jwt-token");

        LoginRequest request = new LoginRequest("admin", "admin");
        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("admin", response.getUsername());
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("Inicio de sesión exitoso", response.getMensaje());
    }

    @Test
    void loginWithInvalidUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest("unknown", "password");
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void loginWithInvalidPassword() {
        String encodedPassword = passwordEncoder.encode("admin");
        UserEntity user = new UserEntity("admin", encodedPassword);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        LoginRequest request = new LoginRequest("admin", "wrongpassword");
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }
}
