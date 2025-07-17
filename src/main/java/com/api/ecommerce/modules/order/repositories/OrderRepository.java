package com.api.ecommerce.modules.order.repositories;

import com.api.ecommerce.modules.order.entities.Order;
import com.api.ecommerce.modules.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUser(User user);
}