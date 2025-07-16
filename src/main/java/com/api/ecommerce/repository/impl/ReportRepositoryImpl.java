package com.api.ecommerce.repository.impl;

import com.api.ecommerce.models.dto.AverageTicketDTO;
import com.api.ecommerce.models.dto.TopCustomerDTO;
import com.api.ecommerce.repository.ReportRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class ReportRepositoryImpl implements ReportRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TopCustomerDTO> findTopCustomers(int limit) {
        String sql = """
            SELECT 
                o.user_id AS customerId,
                u.name AS name,
                SUM(o.total_amount) AS totalSpent
            FROM orders o
            JOIN users u ON u.id = o.user_id
            WHERE o.status = 'PAID'
            GROUP BY o.user_id, u.name
            ORDER BY totalSpent DESC
        """;

        return entityManager.createNativeQuery(sql, "TopCustomerDTOResult")
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<AverageTicketDTO> findAverageTicketPerUser() {
        String sql = """
            SELECT u.email AS userEmail, AVG(o_total.total) AS averageTicket
            FROM (
                SELECT o.id AS order_id, o.user_id, SUM(oi.price_at_purchase * oi.quantity) AS total
                FROM orders o
                JOIN order_items oi ON o.id = oi.order_id
                WHERE o.status = 'PAID'
                GROUP BY o.id
            ) o_total
            JOIN users u ON o_total.user_id = u.id
            GROUP BY u.email
        """;

        return entityManager.createNativeQuery(sql, "AverageTicketMapping")
                .getResultList();
    }

    @Override
    public BigDecimal findMonthlyRevenue() {
        String sql = """
            SELECT SUM(oi.price_at_purchase * oi.quantity)
            FROM orders o
            JOIN order_items oi ON o.id = oi.order_id
            WHERE o.status = 'PAID'
              AND MONTH(o.created_at) = MONTH(CURRENT_DATE())
              AND YEAR(o.created_at) = YEAR(CURRENT_DATE())
        """;

        Object result = entityManager.createNativeQuery(sql).getSingleResult();
        return result != null ? (BigDecimal) result : BigDecimal.ZERO;
    }
}

