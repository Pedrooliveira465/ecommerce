package com.api.ecommerce.models.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequestDTO(
        @NotEmpty(message = "Order must have at least one item")
        @Valid
        List<OrderItemRequestDTO> items
) {}
