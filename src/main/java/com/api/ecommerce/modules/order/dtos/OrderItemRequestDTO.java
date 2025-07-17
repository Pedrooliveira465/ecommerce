package com.api.ecommerce.modules.order.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderItemRequestDTO(
        @NotNull(message = "Product ID cannot be null")
        UUID productId,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity
) {}
