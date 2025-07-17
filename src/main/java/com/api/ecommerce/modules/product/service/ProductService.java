package com.api.ecommerce.modules.product.service;

import com.api.ecommerce.modules.product.exception.ProductNotFoundException;
import com.api.ecommerce.modules.product.entity.Product;
import com.api.ecommerce.modules.product.dtos.CreateProductRequestDTO;
import com.api.ecommerce.modules.product.dtos.ProductDTO;
import com.api.ecommerce.modules.product.dtos.UpdateProductRequestDTO;
import com.api.ecommerce.modules.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDTO create(CreateProductRequestDTO request) {
        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .stockQuantity(request.stockQuantity())
                .category(request.category())
                .build();

        product = productRepository.save(product);
        return mapToDTO(product);
    }

    public List<ProductDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public ProductDTO findById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return mapToDTO(product);
    }

    public ProductDTO update(UUID id, UpdateProductRequestDTO request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setCategory(request.category());

        product = productRepository.save(product);
        return mapToDTO(product);
    }

    public void delete(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    private ProductDTO mapToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory()
        );
    }
}
