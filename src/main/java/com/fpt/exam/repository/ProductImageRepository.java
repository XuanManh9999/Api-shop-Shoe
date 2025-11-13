package com.fpt.exam.repository;

import com.fpt.exam.entity.Product;
import com.fpt.exam.entity.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProduct(Product product);
    
    @Query("SELECT i FROM ProductImage i WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(i.imageUrl) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.product.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:productId IS NULL OR i.product.productId = :productId) AND " +
           "(:isMain IS NULL OR i.isMain = :isMain)")
    Page<ProductImage> searchImages(@Param("keyword") String keyword, 
                                    @Param("productId") Long productId,
                                    @Param("isMain") Boolean isMain,
                                    Pageable pageable);
}

