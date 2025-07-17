package com.api.ecommerce.modules.order.dtos;

import com.api.ecommerce.modules.order.entities.Order;
import com.api.ecommerce.modules.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        List<OrderItemResponseDTO> items,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt
) {
    public static OrderResponseDTO fromEntity(Order order) {
        List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
                .map(OrderItemResponseDTO::fromEntity)
                .toList();

        return new OrderResponseDTO(
                order.getId(),
                itemDTOs,
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
