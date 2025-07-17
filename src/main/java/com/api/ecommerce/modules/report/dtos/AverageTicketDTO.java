package com.api.ecommerce.modules.report.dtos;

import java.math.BigDecimal;

public record AverageTicketDTO(
        String userEmail,
        BigDecimal averageTicket
) {}
