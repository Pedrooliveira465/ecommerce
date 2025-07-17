package com.api.ecommerce.service;

import com.api.ecommerce.modules.product.exception.ProductNotFoundException;
import com.api.ecommerce.modules.product.entity.Product;
import com.api.ecommerce.modules.product.dtos.CreateProductRequestDTO;
import com.api.ecommerce.modules.product.dtos.ProductDTO;
import com.api.ecommerce.modules.product.dtos.UpdateProductRequestDTO;
import com.api.ecommerce.modules.product.repository.ProductRepository;
import com.api.ecommerce.modules.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void shouldCreateProduct() {
        CreateProductRequestDTO request = new CreateProductRequestDTO(
                "Product 1", "Description 1", new BigDecimal("100.00"), 10, "Category 1" // Corrigido "Caregory"
        );

        Product savedProduct = Product.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .stockQuantity(request.stockQuantity())
                .category(request.category())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductDTO result = productService.create(request);

        assertNotNull(result.id());
        assertEquals("Product 1", result.name());                  // Corrigido
        assertEquals("Description 1", result.description());      // Corrigido
        assertEquals(new BigDecimal("100.00"), result.price());
        assertEquals(10, result.stockQuantity());
        assertEquals("Category 1", result.category());            // Adicionado
    }

    @Test
    void shouldFindAllProducts() {
        Product p1 = Product.builder()
                .id(UUID.randomUUID())
                .name("Prod 1")
                .description("Desc 1")
                .price(new BigDecimal("50"))
                .stockQuantity(5)
                .category("Cat 1")
                .build();

        Product p2 = Product.builder()
                .id(UUID.randomUUID())
                .name("Prod 2")
                .description("Desc 2")
                .price(new BigDecimal("150"))
                .stockQuantity(15)
                .category("Cat 2")
                .build();

        when(productRepository.findAll()).thenReturn(List.of(p1, p2));

        List<ProductDTO> products = productService.findAll();

        assertEquals(2, products.size());
        assertTrue(products.stream().anyMatch(p -> p.name().equals("Prod 1")));
        assertTrue(products.stream().anyMatch(p -> p.name().equals("Prod 2")));
    }

    @Test
    void shouldFindProductById() {
        UUID id = UUID.randomUUID();
        Product product = Product.builder()
                .id(id)
                .name("Produto X")
                .description("Descrição X")
                .price(new BigDecimal("99.99"))
                .stockQuantity(20)
                .category("X Category")
                .build();

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        ProductDTO dto = productService.findById(id);

        assertEquals(id, dto.id());
        assertEquals("Produto X", dto.name());
        assertEquals("X Category", dto.category()); // Adicionado
    }

    @Test
    void shouldThrowWhenFindByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(id));
    }

    @Test
    void shouldUpdateProduct() {
        UUID id = UUID.randomUUID();
        Product existing = Product.builder()
                .id(id)
                .name("Old Name")
                .description("Old Desc")
                .price(new BigDecimal("10"))
                .stockQuantity(5)
                .category("Old Category")
                .build();

        UpdateProductRequestDTO updateRequest = new UpdateProductRequestDTO(
                "New Name", "New Desc", new BigDecimal("20"), 15, "New Category"
        );

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        ProductDTO updated = productService.update(id, updateRequest);

        assertEquals("New Name", updated.name());
        assertEquals("New Desc", updated.description());
        assertEquals(new BigDecimal("20"), updated.price());
        assertEquals(15, updated.stockQuantity());
        assertEquals("New Category", updated.category());
    }

    @Test
    void shouldThrowWhenUpdateNotFound() {
        UUID id = UUID.randomUUID();
        UpdateProductRequestDTO updateRequest = new UpdateProductRequestDTO(
                "Name", "Desc", new BigDecimal("20"), 15, "Category"
        );

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.update(id, updateRequest));
    }

    @Test
    void shouldDeleteProduct() {
        UUID id = UUID.randomUUID();

        when(productRepository.existsById(id)).thenReturn(true);

        productService.delete(id);

        verify(productRepository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeleteNotFound() {
        UUID id = UUID.randomUUID();

        when(productRepository.existsById(id)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.delete(id));
    }
}

