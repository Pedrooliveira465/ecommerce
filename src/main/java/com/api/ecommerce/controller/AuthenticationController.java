package com.api.ecommerce.controller;

import com.api.ecommerce.models.User;
import com.api.ecommerce.models.dto.AuthenticationRequestDTO;
import com.api.ecommerce.models.dto.TokenResponseDTO;
import com.api.ecommerce.models.dto.UserRequestDTO;
import com.api.ecommerce.models.dto.UserResponseDTO;
import com.api.ecommerce.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody AuthenticationRequestDTO request) {
        String token = authenticationService.login(request.email(), request.password());
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        User savedUser = authenticationService.register(userRequestDTO.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDTO(savedUser));
    }
}

