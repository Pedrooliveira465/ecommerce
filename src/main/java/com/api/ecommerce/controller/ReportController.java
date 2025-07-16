package com.api.ecommerce.controller;

import com.api.ecommerce.models.dto.AverageTicketDTO;
import com.api.ecommerce.models.dto.TopCustomerDTO;
import com.api.ecommerce.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/top-customers")
    public ResponseEntity<List<TopCustomerDTO>> getTopCustomers(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(reportService.getTopCustomers(limit));
    }

    @GetMapping("/average-ticket")
    public ResponseEntity<List<AverageTicketDTO>> averageTicket() {
        return ResponseEntity.ok(reportService.getAverageTickets());
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<BigDecimal> monthlyRevenue() {
        return ResponseEntity.ok(reportService.getMonthlyRevenue());
    }
}
