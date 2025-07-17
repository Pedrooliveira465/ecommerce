package com.api.ecommerce.modules.product.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDTO(UUID id, String name, String description, BigDecimal price, int stockQuantity, String category) {
}
