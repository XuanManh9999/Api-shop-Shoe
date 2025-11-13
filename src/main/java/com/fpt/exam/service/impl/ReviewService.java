package com.fpt.exam.service.impl;

import com.fpt.exam.entity.Product;
import com.fpt.exam.entity.Review;
import com.fpt.exam.entity.User;
import com.fpt.exam.exception.ResourceNotFoundException;
import com.fpt.exam.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    // Lấy danh sách review có phân trang và tìm kiếm
    public Page<Review> getAllReviews(String keyword, Long productId, Long userId, Integer rating, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if ((keyword != null && !keyword.trim().isEmpty()) || productId != null || userId != null || rating != null) {
            return reviewRepository.searchReviews(keyword, productId, userId, rating, pageable);
        }
        return reviewRepository.findAll(pageable);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
    }

    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findByUser(user);
    }

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review updateReview(Long id, Review newReview) {
        Review review = getReviewById(id);
        review.setRating(newReview.getRating());
        review.setComment(newReview.getComment());
        if (newReview.getProduct() != null) {
            review.setProduct(newReview.getProduct());
        }
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        Review review = getReviewById(id);
        reviewRepository.delete(review);
    }
}

