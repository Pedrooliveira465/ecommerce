package com.api.ecommerce.models.dto;

import com.api.ecommerce.models.User;
import com.api.ecommerce.models.enums.Role;

import java.util.UUID;

public record UserResponseDTO(UUID id, String name, String email, Role role) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
