package com.api.ecommerce.modules.report.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record TopCustomerDTO(UUID customerId, String name, BigDecimal totalSpent) {}
