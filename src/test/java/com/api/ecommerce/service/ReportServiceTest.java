package com.api.ecommerce.service;

import com.api.ecommerce.modules.report.dtos.AverageTicketDTO;
import com.api.ecommerce.modules.report.dtos.TopCustomerDTO;
import com.api.ecommerce.modules.report.repositories.ReportRepositoryCustom;
import com.api.ecommerce.modules.report.services.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    private ReportRepositoryCustom reportRepository;
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        reportRepository = mock(ReportRepositoryCustom.class);
        reportService = new ReportService(reportRepository);
    }

    @Test
    void shouldReturnTopCustomers() {
        UUID id1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID id2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

        List<TopCustomerDTO> expected = List.of(
                new TopCustomerDTO(id1, "User One", BigDecimal.valueOf(1000)),
                new TopCustomerDTO(id2, "User Two", BigDecimal.valueOf(800))
        );

        when(reportRepository.findTopCustomers(2)).thenReturn(expected);

        List<TopCustomerDTO> result = reportService.getTopCustomers(2);

        assertEquals(expected, result);
        verify(reportRepository, times(1)).findTopCustomers(2);
    }

    @Test
    void shouldReturnAverageTickets() {
        List<AverageTicketDTO> expected = List.of(
                new AverageTicketDTO("user1@example.com", BigDecimal.valueOf(200)),
                new AverageTicketDTO("user2@example.com", BigDecimal.valueOf(150))
        );

        when(reportRepository.findAverageTicketPerUser()).thenReturn(expected);

        List<AverageTicketDTO> result = reportService.getAverageTickets();

        assertEquals(expected, result);
        verify(reportRepository, times(1)).findAverageTicketPerUser();
    }

    @Test
    void shouldReturnMonthlyRevenue() {
        BigDecimal expectedRevenue = BigDecimal.valueOf(10000);

        when(reportRepository.findMonthlyRevenue()).thenReturn(expectedRevenue);

        BigDecimal result = reportService.getMonthlyRevenue();

        assertEquals(expectedRevenue, result);
        verify(reportRepository, times(1)).findMonthlyRevenue();
    }
}

