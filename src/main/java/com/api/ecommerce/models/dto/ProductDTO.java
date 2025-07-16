package com.api.ecommerce.models.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDTO(UUID id, String name, String description, BigDecimal price, int stockQuantity, String category) {
}
