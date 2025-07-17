package com.api.ecommerce.service;

import com.api.ecommerce.modules.order.exceptions.InsufficientStockException;
import com.api.ecommerce.modules.order.exceptions.OrderAlreadyPaidException;
import com.api.ecommerce.modules.order.exceptions.OrderNotFoundException;
import com.api.ecommerce.modules.product.exception.ProductNotFoundException;
import com.api.ecommerce.modules.order.entities.Order;
import com.api.ecommerce.modules.order.entities.OrderItem;
import com.api.ecommerce.modules.product.entity.Product;
import com.api.ecommerce.modules.user.entities.User;
import com.api.ecommerce.modules.order.dtos.OrderItemRequestDTO;
import com.api.ecommerce.modules.order.dtos.OrderRequestDTO;
import com.api.ecommerce.modules.order.dtos.OrderResponseDTO;
import com.api.ecommerce.modules.order.enums.OrderStatus;
import com.api.ecommerce.modules.order.repositories.OrderRepository;
import com.api.ecommerce.modules.order.services.OrderService;
import com.api.ecommerce.modules.product.repository.ProductRepository;
import com.api.ecommerce.modules.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);

        orderService = new OrderService(orderRepository, userRepository, productRepository);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user@example.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    private User createUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("user@example.com");
        return user;
    }

    private Product createProduct(UUID id, int stock, BigDecimal price) {
        Product p = new Product();
        p.setId(id);
        p.setStockQuantity(stock);
        p.setPrice(price);
        p.setName("Produto Teste");
        return p;
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        User user = createUser();
        UUID prodId = UUID.randomUUID();
        Product product = createProduct(prodId, 10, new BigDecimal("100"));

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(productRepository.findById(prodId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        OrderItemRequestDTO itemDTO = new OrderItemRequestDTO(prodId, 5);
        OrderRequestDTO requestDTO = new OrderRequestDTO(List.of(itemDTO));

        OrderResponseDTO response = orderService.createOrderForAuthenticatedUser(requestDTO);

        assertEquals(OrderStatus.PENDING, response.status());
        assertEquals(new BigDecimal("500"), response.totalAmount());
        assertEquals(1, response.items().size());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void shouldThrowIfProductNotFoundWhenCreatingOrder() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(createUser()));
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        OrderItemRequestDTO itemDTO = new OrderItemRequestDTO(UUID.randomUUID(), 1);
        OrderRequestDTO requestDTO = new OrderRequestDTO(List.of(itemDTO));

        assertThrows(ProductNotFoundException.class, () -> orderService.createOrderForAuthenticatedUser(requestDTO));
    }

    @Test
    void shouldThrowIfInsufficientStockWhenCreatingOrder() {
        User user = createUser();
        UUID prodId = UUID.randomUUID();
        Product product = createProduct(prodId, 2, new BigDecimal("100"));

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(productRepository.findById(prodId)).thenReturn(Optional.of(product));

        OrderItemRequestDTO itemDTO = new OrderItemRequestDTO(prodId, 5);
        OrderRequestDTO requestDTO = new OrderRequestDTO(List.of(itemDTO));

        assertThrows(InsufficientStockException.class, () -> orderService.createOrderForAuthenticatedUser(requestDTO));
    }

    @Test
    void shouldListOrdersForAuthenticatedUser() {
        User user = createUser();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        Order order1 = Order.builder()
                .id(UUID.randomUUID())
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("100"))
                .user(user)
                .items(Collections.emptyList())
                .build();

        Order order2 = Order.builder()
                .id(UUID.randomUUID())
                .status(OrderStatus.PAID)
                .totalAmount(new BigDecimal("200"))
                .user(user)
                .items(Collections.emptyList())
                .build();


        when(orderRepository.findByUser(user)).thenReturn(List.of(order1, order2));

        List<OrderResponseDTO> orders = orderService.listOrdersForAuthenticatedUser();

        assertEquals(2, orders.size());
    }

    @Test
    void shouldPayOrderSuccessfully() {
        User user = createUser();
        UUID orderId = UUID.randomUUID();
        Product product = createProduct(UUID.randomUUID(), 10, new BigDecimal("100"));

        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(5)
                .priceAtPurchase(product.getPrice())
                .build();

        Order order = Order.builder()
                .id(orderId)
                .user(user)
                .status(OrderStatus.PENDING)
                .items(List.of(orderItem))
                .totalAmount(new BigDecimal("500"))
                .build();

        orderItem.setOrder(order);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        OrderResponseDTO response = orderService.payOrderForAuthenticatedUser(orderId);

        assertEquals(OrderStatus.PAID, response.status());
        assertEquals(5, product.getStockQuantity());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productRepository).save(product);
    }


    @Test
    void shouldThrowIfOrderNotFoundOrNotBelongToUserWhenPaying() {
        User user = createUser();
        UUID orderId = UUID.randomUUID();

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.payOrderForAuthenticatedUser(orderId));
    }

    @Test
    void shouldThrowIfOrderAlreadyPaid() {
        User user = createUser();
        UUID orderId = UUID.randomUUID();

        Order order = Order.builder()
                .id(orderId)
                .user(user)
                .status(OrderStatus.PAID)
                .build();

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(OrderAlreadyPaidException.class, () -> orderService.payOrderForAuthenticatedUser(orderId));
    }

    @Test
    void shouldCancelOrderAndThrowIfInsufficientStockWhenPaying() {
        User user = createUser();
        UUID orderId = UUID.randomUUID();
        Product product = createProduct(UUID.randomUUID(), 2, new BigDecimal("100"));

        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(5)
                .priceAtPurchase(product.getPrice())
                .build();

        Order order = Order.builder()
                .id(orderId)
                .user(user)
                .status(OrderStatus.PENDING)
                .items(List.of(orderItem))
                .build();

        orderItem.setOrder(order);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        assertThrows(InsufficientStockException.class, () -> orderService.payOrderForAuthenticatedUser(orderId));

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderRepository).save(order);
    }
}
