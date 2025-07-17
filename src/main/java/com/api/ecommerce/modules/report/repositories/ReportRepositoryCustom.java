package com.api.ecommerce.modules.report.repositories;

import com.api.ecommerce.modules.report.dtos.AverageTicketDTO;
import com.api.ecommerce.modules.report.dtos.TopCustomerDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ReportRepositoryCustom {
    List<TopCustomerDTO> findTopCustomers(int limit);
    List<AverageTicketDTO> findAverageTicketPerUser();
    BigDecimal findMonthlyRevenue();
}
