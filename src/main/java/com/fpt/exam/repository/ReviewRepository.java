package com.fpt.exam.repository;

import com.fpt.exam.entity.Product;
import com.fpt.exam.entity.Review;
import com.fpt.exam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);
    List<Review> findByUser(User user);
}

