package com.api.ecommerce.controller;

import com.api.ecommerce.config.SecurityConfig;
import com.api.ecommerce.models.dto.AverageTicketDTO;
import com.api.ecommerce.models.dto.TopCustomerDTO;
import com.api.ecommerce.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class ReportController {

    private final ReportService reportService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/top-customers")
    public ResponseEntity<List<TopCustomerDTO>> getTopCustomers(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(reportService.getTopCustomers(limit));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/average-ticket")
    public ResponseEntity<List<AverageTicketDTO>> averageTicket() {
        return ResponseEntity.ok(reportService.getAverageTickets());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/monthly-revenue")
    public ResponseEntity<BigDecimal> monthlyRevenue() {
        return ResponseEntity.ok(reportService.getMonthlyRevenue());
    }
}
