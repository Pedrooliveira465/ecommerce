package com.api.ecommerce.service;

import com.api.ecommerce.modules.user.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private final String secretKey = "12345678901234567890123456789012";

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();

        Field secretField = JwtService.class.getDeclaredField("secretKey");
        secretField.setAccessible(true);
        secretField.set(jwtService, secretKey);
    }

    @Test
    void shouldGenerateValidTokenAndExtractUsername() {
        UserDetails userDetails = new User(
                "user@example.com",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        String extractedUsername = jwtService.extractUsername(token);
        assertEquals("user@example.com", extractedUsername);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void shouldReturnFalseForInvalidUsername() {
        UserDetails realUser = new User("real@example.com", "pass", List.of());
        UserDetails fakeUser = new User("fake@example.com", "pass", List.of());

        String token = jwtService.generateToken(realUser);

        assertFalse(jwtService.isTokenValid(token, fakeUser));
    }

    @Test
    void shouldDetectExpiredToken() throws Exception {
        String expiredToken = io.jsonwebtoken.Jwts.builder()
                .setSubject("user@example.com")
                .setIssuedAt(new java.util.Date(System.currentTimeMillis() - 1000 * 60 * 60 * 25)) // 25h atrás
                .setExpiration(new java.util.Date(System.currentTimeMillis() - 1000 * 60 * 60)) // expirado há 1h
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()), io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();

        UserDetails userDetails = new User("user@example.com", "pass", List.of());

        assertFalse(jwtService.isTokenValid(expiredToken, userDetails));
    }
}

