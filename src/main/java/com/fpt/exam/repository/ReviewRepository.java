package com.fpt.exam.repository;

import com.fpt.exam.entity.Product;
import com.fpt.exam.entity.Review;
import com.fpt.exam.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);
    List<Review> findByUser(User user);
    
    @Query("SELECT r FROM Review r WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(r.comment) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.product.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.user.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.user.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:productId IS NULL OR r.product.productId = :productId) AND " +
           "(:userId IS NULL OR r.user.userId = :userId) AND " +
           "(:rating IS NULL OR r.rating = :rating)")
    Page<Review> searchReviews(@Param("keyword") String keyword, 
                               @Param("productId") Long productId,
                               @Param("userId") Long userId,
                               @Param("rating") Integer rating,
                               Pageable pageable);
}

