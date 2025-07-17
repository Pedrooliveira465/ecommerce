package com.api.ecommerce.modules.order.services;

import com.api.ecommerce.modules.order.entities.Order;
import com.api.ecommerce.modules.order.entities.OrderItem;
import com.api.ecommerce.modules.product.entity.Product;
import com.api.ecommerce.modules.user.entities.User;
import com.api.ecommerce.modules.order.dtos.OrderRequestDTO;
import com.api.ecommerce.modules.order.dtos.OrderResponseDTO;
import com.api.ecommerce.modules.order.enums.OrderStatus;
import com.api.ecommerce.modules.order.exceptions.InsufficientStockException;
import com.api.ecommerce.modules.order.exceptions.OrderAlreadyPaidException;
import com.api.ecommerce.modules.order.exceptions.OrderNotFoundException;
import com.api.ecommerce.modules.order.repositories.OrderRepository;
import com.api.ecommerce.modules.product.exception.ProductNotFoundException;
import com.api.ecommerce.modules.product.repository.ProductRepository;
import com.api.ecommerce.modules.user.exceptions.UserNotAuthenticatedException;
import com.api.ecommerce.modules.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponseDTO createOrderForAuthenticatedUser(OrderRequestDTO orderRequest) {
        User user = getAuthenticatedUser();

        List<OrderItem> items = orderRequest.items().stream().map(itemDTO -> {
            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));

            if (product.getStockQuantity() < itemDTO.quantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }

            return OrderItem.builder()
                    .product(product)
                    .quantity(itemDTO.quantity())
                    .priceAtPurchase(product.getPrice())
                    .build();
        }).toList();

        BigDecimal totalAmount = items.stream()
                .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .user(user)
                .items(items)
                .status(OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .build();

        items.forEach(item -> item.setOrder(order));
        orderRepository.save(order);

        return OrderResponseDTO.fromEntity(order);
    }

    public List<OrderResponseDTO> listOrdersForAuthenticatedUser() {
        User user = getAuthenticatedUser();
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDTO payOrderForAuthenticatedUser(UUID orderId) {
        User user = getAuthenticatedUser();

        Order order = orderRepository.findById(orderId)
                .filter(o -> o.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new OrderNotFoundException("Order not found or does not belong to user"));

        if (order.getStatus() == OrderStatus.PAID) {
            throw new OrderAlreadyPaidException("Order already paid");
        }

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            if (product.getStockQuantity() < item.getQuantity()) {
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }
        }

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        return OrderResponseDTO.fromEntity(order);
    }

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotAuthenticatedException("Authenticated user not found"));
    }
}

