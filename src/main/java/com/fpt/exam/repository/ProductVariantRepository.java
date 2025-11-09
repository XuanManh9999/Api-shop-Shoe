package com.fpt.exam.repository;

import com.fpt.exam.entity.Product;
import com.fpt.exam.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    List<ProductVariant> findByProduct(Product product);
    List<ProductVariant> findByColorIgnoreCase(String color);
}

