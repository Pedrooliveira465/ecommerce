package com.api.ecommerce.controller;

import com.api.ecommerce.modules.report.dtos.AverageTicketDTO;
import com.api.ecommerce.modules.report.dtos.TopCustomerDTO;
import com.api.ecommerce.modules.report.controller.ReportController;
import com.api.ecommerce.modules.user.services.JwtService;
import com.api.ecommerce.modules.report.services.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<TopCustomerDTO> topCustomers;
    private List<AverageTicketDTO> averageTickets;
    private BigDecimal monthlyRevenue;

    @BeforeEach
    void setup() {
        topCustomers = List.of(
                new TopCustomerDTO(UUID.randomUUID(), "Customer1", BigDecimal.valueOf(1000)),
                new TopCustomerDTO(UUID.randomUUID(), "Customer2", BigDecimal.valueOf(900))
        );

        averageTickets = List.of(
                new AverageTicketDTO("customer1@example.com", BigDecimal.valueOf(250)),
                new AverageTicketDTO("customer2@example.com", BigDecimal.valueOf(300))
        );

        monthlyRevenue = BigDecimal.valueOf(5000);
    }

    @Test
    void shouldReturnTopCustomers() throws Exception {
        Mockito.when(reportService.getTopCustomers(anyInt())).thenReturn(topCustomers);

        mockMvc.perform(get("/api/reports/top-customers")
                        .param("limit", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Customer1"))
                .andExpect(jsonPath("$[0].totalSpent").value(1000))
                .andExpect(jsonPath("$[1].name").value("Customer2"))
                .andExpect(jsonPath("$[1].totalSpent").value(900));
    }

    @Test
    void shouldReturnAverageTickets() throws Exception {
        Mockito.when(reportService.getAverageTickets()).thenReturn(averageTickets);

        mockMvc.perform(get("/api/reports/average-ticket")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userEmail").value("customer1@example.com"))
                .andExpect(jsonPath("$[0].averageTicket").value(250))
                .andExpect(jsonPath("$[1].userEmail").value("customer2@example.com"))
                .andExpect(jsonPath("$[1].averageTicket").value(300));
    }

    @Test
    void shouldReturnMonthlyRevenue() throws Exception {
        Mockito.when(reportService.getMonthlyRevenue()).thenReturn(monthlyRevenue);

        mockMvc.perform(get("/api/reports/monthly-revenue")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5000"));
    }
}

