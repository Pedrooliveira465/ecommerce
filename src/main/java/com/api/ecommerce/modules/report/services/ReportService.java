package com.api.ecommerce.modules.report.services;

import com.api.ecommerce.modules.report.dtos.AverageTicketDTO;
import com.api.ecommerce.modules.report.dtos.TopCustomerDTO;
import com.api.ecommerce.modules.report.repositories.ReportRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepositoryCustom reportRepository;

    public List<TopCustomerDTO> getTopCustomers(int limit) {
        return reportRepository.findTopCustomers(limit);
    }

    public List<AverageTicketDTO> getAverageTickets() {
        return reportRepository.findAverageTicketPerUser();
    }

    public BigDecimal getMonthlyRevenue() {
        return reportRepository.findMonthlyRevenue();
    }
}