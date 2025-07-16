package com.api.ecommerce.controller;

import com.api.ecommerce.config.SecurityConfig;
import com.api.ecommerce.models.dto.OrderRequestDTO;
import com.api.ecommerce.models.dto.OrderResponseDTO;
import com.api.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Parameter(description = "Order creation payload", required = true)
            @Valid @RequestBody OrderRequestDTO dto
    ) {
        return ResponseEntity.ok(orderService.createOrderForAuthenticatedUser(dto));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> listOrders() {
        return ResponseEntity.ok(orderService.listOrdersForAuthenticatedUser());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderResponseDTO> payOrder(
            @Parameter(description = "Order ID to be paid", required = true)
            @PathVariable UUID orderId
    ) {
        return ResponseEntity.ok(orderService.payOrderForAuthenticatedUser(orderId));
    }
}

