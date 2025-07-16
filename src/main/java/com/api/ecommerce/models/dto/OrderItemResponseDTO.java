package com.api.ecommerce.models.dto;

import com.api.ecommerce.models.OrderItem;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDTO(
        UUID productId,
        String productName,
        Integer quantity,
        BigDecimal priceAtPurchase
) {
    public static OrderItemResponseDTO fromEntity(OrderItem item) {
        return new OrderItemResponseDTO(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPriceAtPurchase()
        );
    }
}