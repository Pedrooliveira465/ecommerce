package com.api.ecommerce.models.dto;

import com.api.ecommerce.models.User;
import com.api.ecommerce.models.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
        @NotEmpty(message = "Name cannot be empty")
        String name,

        @Email(message = "Invalid email format")
        @NotEmpty(message = "Email cannot be empty")
        String email,

        @NotNull(message = "Password cannot be null")
        String password,

        @NotNull(message = "Role cannot be null")
        Role role
) {
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .role(role)
                .build();
    }
}
