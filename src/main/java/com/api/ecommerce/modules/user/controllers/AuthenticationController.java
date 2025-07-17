package com.api.ecommerce.modules.user.controllers;

import com.api.ecommerce.config.SecurityConfig;
import com.api.ecommerce.modules.user.entities.User;
import com.api.ecommerce.modules.user.dtos.AuthenticationRequestDTO;
import com.api.ecommerce.modules.user.dtos.TokenResponseDTO;
import com.api.ecommerce.modules.user.dtos.UserRequestDTO;
import com.api.ecommerce.modules.user.dtos.UserResponseDTO;
import com.api.ecommerce.modules.user.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "Handle all operations related to authentication and user registration")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Authenticate and generate JWT token")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody AuthenticationRequestDTO request) {
        String token = authenticationService.login(request.email(), request.password());
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        User savedUser = authenticationService.register(userRequestDTO.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDTO(savedUser));
    }
}


