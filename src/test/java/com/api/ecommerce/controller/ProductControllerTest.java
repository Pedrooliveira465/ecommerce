package com.api.ecommerce.controller;

import com.api.ecommerce.models.dto.CreateProductRequestDTO;
import com.api.ecommerce.models.dto.ProductDTO;
import com.api.ecommerce.models.dto.UpdateProductRequestDTO;
import com.api.ecommerce.service.JwtService;
import com.api.ecommerce.service.ProductService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID productId;
    private ProductDTO productDTO;

    @BeforeEach
    void setup() {
        productId = UUID.randomUUID();

        productDTO = new ProductDTO(
                productId,
                "Test Product",
                "Product Description",
                BigDecimal.valueOf(99.99),
                10,
                "Test Category"
        );
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        Mockito.when(productService.findAll()).thenReturn(List.of(productDTO));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(productId.toString()))
                .andExpect(jsonPath("$[0].name").value("Test Product"))
                .andExpect(jsonPath("$[0].price").value(99.99))
                .andExpect(jsonPath("$[0].stockQuantity").value(10))
                .andExpect(jsonPath("$[0].category").value("Test Category")); // NOVO
    }

    @Test
    void shouldGetProductById() throws Exception {
        Mockito.when(productService.findById(productId)).thenReturn(productDTO);

        mockMvc.perform(get("/api/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(99.99))
                .andExpect(jsonPath("$.stockQuantity").value(10))
                .andExpect(jsonPath("$.category").value("Test Category")); // NOVO
    }

    @Test
    void shouldCreateProduct() throws Exception {
        CreateProductRequestDTO createRequest = new CreateProductRequestDTO(
                "New Product",
                "New product description",
                BigDecimal.valueOf(150.00),
                20,
                "New Category"
        );

        ProductDTO createdProduct = new ProductDTO(
                UUID.randomUUID(),
                createRequest.name(),
                createRequest.description(),
                createRequest.price(),
                createRequest.stockQuantity(),
                createRequest.category()
        );

        Mockito.when(productService.create(any(CreateProductRequestDTO.class))).thenReturn(createdProduct);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.price").value(150.00))
                .andExpect(jsonPath("$.stockQuantity").value(20))
                .andExpect(jsonPath("$.category").value("New Category")); // NOVO
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        UpdateProductRequestDTO updateRequest = new UpdateProductRequestDTO(
                "Updated Product",
                "Updated description",
                BigDecimal.valueOf(120.00),
                15,
                "Update Category"
        );

        ProductDTO updatedProduct = new ProductDTO(
                productId,
                updateRequest.name(),
                updateRequest.description(),
                updateRequest.price(),
                updateRequest.stockQuantity(),
                updateRequest.category()
        );

        Mockito.when(productService.update(eq(productId), any(UpdateProductRequestDTO.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(120.00))
                .andExpect(jsonPath("$.stockQuantity").value(15))
                .andExpect(jsonPath("$.category").value("Update Category")); // NOVO
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        Mockito.doNothing().when(productService).delete(productId);

        mockMvc.perform(delete("/api/products/{id}", productId))
                .andExpect(status().isNoContent());
    }
}

