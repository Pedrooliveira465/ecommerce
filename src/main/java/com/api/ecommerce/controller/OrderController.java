package com.api.ecommerce.controller;

import com.api.ecommerce.config.SecurityConfig;
import com.api.ecommerce.models.dto.OrderRequestDTO;
import com.api.ecommerce.models.dto.OrderResponseDTO;
import com.api.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Orders", description = "Manage customer orders")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Create new order",
            description = "Creates a new order with the provided product IDs and quantities.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order created",
                            content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
            }
    )
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Parameter(description = "Order creation payload", required = true)
            @Valid @RequestBody OrderRequestDTO dto
    ) {
        return ResponseEntity.ok(orderService.createOrderForAuthenticatedUser(dto));
    }

    @Operation(
            summary = "List authenticated user's orders",
            description = "Fetches all orders made by the logged-in user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of orders",
                            content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
            }
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> listOrders() {
        return ResponseEntity.ok(orderService.listOrdersForAuthenticatedUser());
    }

    @Operation(
            summary = "Pay for order",
            description = "Processes payment for the specified order ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order paid",
                            content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Cannot pay for this order", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
            }
    )
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderResponseDTO> payOrder(
            @Parameter(description = "Order ID to be paid", required = true)
            @PathVariable UUID orderId
    ) {
        return ResponseEntity.ok(orderService.payOrderForAuthenticatedUser(orderId));
    }
}