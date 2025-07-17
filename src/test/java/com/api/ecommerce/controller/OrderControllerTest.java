package com.api.ecommerce.controller;

import com.api.ecommerce.modules.order.dtos.OrderItemRequestDTO;
import com.api.ecommerce.modules.order.dtos.OrderItemResponseDTO;
import com.api.ecommerce.modules.order.dtos.OrderRequestDTO;
import com.api.ecommerce.modules.order.dtos.OrderResponseDTO;
import com.api.ecommerce.modules.order.enums.OrderStatus;
import com.api.ecommerce.modules.order.controller.OrderController;
import com.api.ecommerce.modules.user.services.JwtService;
import com.api.ecommerce.modules.order.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID orderId;
    private OrderResponseDTO orderResponseDTO;

    @BeforeEach
    void setup() {
        orderId = UUID.randomUUID();

        OrderItemResponseDTO itemResponse = new OrderItemResponseDTO(
                UUID.randomUUID(),
                "Product 1",
                2,                        // autoboxing para Integer
                BigDecimal.valueOf(50.0)
        );

        orderResponseDTO = new OrderResponseDTO(
                orderId,
                List.of(itemResponse),
                BigDecimal.valueOf(100.0),
                OrderStatus.PENDING,
                LocalDateTime.now()
        );
    }

    @Test
    void shouldCreateOrder() throws Exception {
        OrderItemRequestDTO itemRequest = new OrderItemRequestDTO(UUID.randomUUID(), 2);
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO(List.of(itemRequest));

        Mockito.when(orderService.createOrderForAuthenticatedUser(any(OrderRequestDTO.class)))
                .thenReturn(orderResponseDTO);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderResponseDTO.id().toString()))
                .andExpect(jsonPath("$.items[0].productName").value("Product 1"))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.totalAmount").value(100.0))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldListOrders() throws Exception {
        Mockito.when(orderService.listOrdersForAuthenticatedUser())
                .thenReturn(List.of(orderResponseDTO));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(orderResponseDTO.id().toString()))
                .andExpect(jsonPath("$[0].items[0].productName").value("Product 1"));
    }

    @Test
    void shouldPayOrder() throws Exception {
        Mockito.when(orderService.payOrderForAuthenticatedUser(eq(orderId)))
                .thenReturn(orderResponseDTO);

        mockMvc.perform(post("/api/orders/{orderId}/pay", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderResponseDTO.id().toString()))
                .andExpect(jsonPath("$.status").value("PENDING")); // ou PAID dependendo do seu mock
    }
}

