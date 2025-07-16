package com.api.ecommerce.service;

import com.api.ecommerce.models.dto.AverageTicketDTO;
import com.api.ecommerce.models.dto.TopCustomerDTO;
import com.api.ecommerce.repository.ReportRepositoryCustom;
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