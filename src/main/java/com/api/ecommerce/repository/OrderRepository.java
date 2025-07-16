package com.api.ecommerce.repository;

import com.api.ecommerce.models.Order;
import com.api.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUser(User user);
}