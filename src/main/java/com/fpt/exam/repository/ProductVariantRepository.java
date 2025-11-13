package com.fpt.exam.repository;

import com.fpt.exam.entity.Product;
import com.fpt.exam.entity.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    List<ProductVariant> findByProduct(Product product);
    List<ProductVariant> findByColorIgnoreCase(String color);
    
    @Query("SELECT v FROM ProductVariant v WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(v.size) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(v.color) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(v.product.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:productId IS NULL OR v.product.productId = :productId)")
    Page<ProductVariant> searchVariants(@Param("keyword") String keyword, 
                                        @Param("productId") Long productId, 
                                        Pageable pageable);
    
    // Statistics queries
    @Query("SELECT v.product.productId, v.product.name, v.size, v.color, v.stock " +
           "FROM ProductVariant v " +
           "WHERE v.stock <= :threshold " +
           "ORDER BY v.stock ASC")
    List<Object[]> findLowStockVariants(@Param("threshold") Integer threshold);
}

