package com.api.ecommerce.controller;

import com.api.ecommerce.config.SecurityConfig;
import com.api.ecommerce.models.dto.AverageTicketDTO;
import com.api.ecommerce.models.dto.TopCustomerDTO;
import com.api.ecommerce.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Report Controller", description = "Handle all operations related to Reports")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class ReportController {

    private final ReportService reportService;

    @Operation(
            summary = "Get top customers",
            description = "Retrieve the top customers who have spent the most, limited by the specified number."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of top customers retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/top-customers")
    public ResponseEntity<List<TopCustomerDTO>> getTopCustomers(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(reportService.getTopCustomers(limit));
    }

    @Operation(
            summary = "Get average ticket",
            description = "Retrieve the average order value (ticket) per user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Average ticket values retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/average-ticket")
    public ResponseEntity<List<AverageTicketDTO>> averageTicket() {
        return ResponseEntity.ok(reportService.getAverageTickets());
    }

    @Operation(
            summary = "Get monthly revenue",
            description = "Retrieve the total revenue for the current month."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monthly revenue retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/monthly-revenue")
    public ResponseEntity<BigDecimal> monthlyRevenue() {
        return ResponseEntity.ok(reportService.getMonthlyRevenue());
    }
}
