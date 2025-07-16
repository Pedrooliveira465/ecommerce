package com.api.ecommerce.models.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TopCustomerDTO(UUID customerId, String name, BigDecimal totalSpent) {}
