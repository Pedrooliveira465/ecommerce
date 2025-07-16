package com.api.ecommerce.models.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateProductRequestDTO(
        @NotBlank(message = "Product name cannot be empty")
        @Valid
        String name,

        @NotBlank(message = "Product name cannot be empty")
        @Valid
        String description,

        @PositiveOrZero(message = "Price must be zero or positive")
        @NotNull(message = "Price cannot be null")
        @Valid
        BigDecimal price,

        @PositiveOrZero(message = "The quantity in stock must be zero or positive")
        @NotNull(message = "Price cannot be null")
        @Valid
        Integer stockQuantity,

        @NotBlank(message = "Product name cannot be empty")
        @Valid
        String category
) {}
