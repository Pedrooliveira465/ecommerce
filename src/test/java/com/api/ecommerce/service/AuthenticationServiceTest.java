package com.api.ecommerce.service;

import com.api.ecommerce.modules.user.exceptions.EmailAlreadyRegisteredException;
import com.api.ecommerce.modules.user.entities.User;
import com.api.ecommerce.modules.user.enums.Role;
import com.api.ecommerce.modules.user.repositories.UserRepository;
import com.api.ecommerce.modules.user.services.AuthenticationService;
import com.api.ecommerce.modules.user.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    private AuthenticationService authService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authManager;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtService = mock(JwtService.class);
        authManager = mock(AuthenticationManager.class);

        authService = new AuthenticationService(userRepository, passwordEncoder, jwtService, authManager);
    }

    @Test
    void shouldRegisterNewUserSuccessfully() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("1234");
        user.setRole(Role.USER);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("hashed1234");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User saved = authService.register(user);

        assertEquals("test@example.com", saved.getEmail());
        assertEquals("hashed1234", saved.getPassword());

        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyRegistered() {
        User user = new User();
        user.setEmail("existing@example.com");

        when(userRepository.findByEmail("existing@example.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyRegisteredException.class, () -> authService.register(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldLoginSuccessfullyAndReturnJwtToken() {
        String email = "user@example.com";
        String rawPassword = "1234";
        String encodedPassword = "encoded1234";
        String expectedToken = "jwt-token";

        User dbUser = new User();
        dbUser.setEmail(email);
        dbUser.setPassword(encodedPassword);
        dbUser.setRole(Role.USER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(dbUser));
        when(jwtService.generateToken(any())).thenReturn(expectedToken);

        String token = authService.login(email, rawPassword);

        assertEquals(expectedToken, token);
        verify(authManager).authenticate(
                new UsernamePasswordAuthenticationToken(email, rawPassword)
        );
        verify(jwtService).generateToken(any());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnLogin() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        Authentication mockAuth = mock(Authentication.class);
        when(authManager.authenticate(any())).thenReturn(mockAuth);

        assertThrows(UsernameNotFoundException.class, () ->
                authService.login("missing@example.com", "pass")
        );
    }
}
