package com.api.ecommerce.modules.product.repository;

import com.api.ecommerce.modules.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
