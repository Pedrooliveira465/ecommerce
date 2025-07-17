package com.api.ecommerce.modules.user.dtos;

import com.api.ecommerce.modules.user.entities.User;
import com.api.ecommerce.modules.user.enums.Role;

import java.util.UUID;

public record UserResponseDTO(UUID id, String name, String email, Role role) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
