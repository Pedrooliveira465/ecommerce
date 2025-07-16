package com.api.ecommerce.controller;

import com.api.ecommerce.models.dto.OrderRequestDTO;
import com.api.ecommerce.models.dto.OrderResponseDTO;
import com.api.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Parameter(description = "Order creation payload", required = true)
            @Valid @RequestBody OrderRequestDTO dto
    ) {
        return ResponseEntity.ok(orderService.createOrderForAuthenticatedUser(dto));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> listOrders() {
        return ResponseEntity.ok(orderService.listOrdersForAuthenticatedUser());
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderResponseDTO> payOrder(
            @Parameter(description = "Order ID to be paid", required = true)
            @PathVariable UUID orderId
    ) {
        return ResponseEntity.ok(orderService.payOrderForAuthenticatedUser(orderId));
    }
}

