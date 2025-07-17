package com.api.ecommerce.modules.product.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateProductRequestDTO(
        @NotBlank(message = "Product name cannot be empty")
        @Valid
        String name,

        @NotBlank(message = "Description is required")
        @Valid
        String description,

        @PositiveOrZero(message = "Price must be zero or positive")
        @Valid
        BigDecimal price,

        @PositiveOrZero(message = "The quantity in stock must be zero or positive")
        @Valid
        Integer stockQuantity,

        @NotBlank(message = "Category is required")
        @Valid
        String category
) {}
