package com.api.ecommerce.repository;

import com.api.ecommerce.models.dto.AverageTicketDTO;
import com.api.ecommerce.models.dto.TopCustomerDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ReportRepositoryCustom {
    List<TopCustomerDTO> findTopCustomers(int limit);
    List<AverageTicketDTO> findAverageTicketPerUser();
    BigDecimal findMonthlyRevenue();
}
