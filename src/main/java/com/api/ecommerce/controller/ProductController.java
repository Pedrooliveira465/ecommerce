package com.api.ecommerce.controller;

import com.api.ecommerce.config.SecurityConfig;
import com.api.ecommerce.models.dto.CreateProductRequestDTO;
import com.api.ecommerce.models.dto.ProductDTO;
import com.api.ecommerce.models.dto.UpdateProductRequestDTO;
import com.api.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products Controller", description = "Handle all operations related to Products")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products", description = "Retrieve a list of all available products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products retrieved successfully")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ProductDTO>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a product by its UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ProductDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @Operation(summary = "Create a new product", description = "Create a new product. Only admins can perform this operation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody CreateProductRequestDTO request) {
        ProductDTO created = productService.create(request);
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Update an existing product", description = "Update product details by ID. Only admins can perform this operation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDTO> update(@PathVariable UUID id, @Valid @RequestBody UpdateProductRequestDTO request) {
        ProductDTO updated = productService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a product", description = "Delete a product by its ID. Only admins can perform this operation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

