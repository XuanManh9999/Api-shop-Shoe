package com.fpt.exam.controller;

import com.fpt.exam.dto.ApiResponse;
import com.fpt.exam.dto.PaginationResponse;
import com.fpt.exam.entity.Review;
import com.fpt.exam.service.impl.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<Review>>> getAllReviews(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer rating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "reviewId") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Page<Review> reviewPage = reviewService.getAllReviews(keyword, productId, userId, rating, page, size, sortBy, sortDir);
            PaginationResponse<Review> paginationResponse = PaginationResponse.of(
                    reviewPage.getContent(),
                    reviewPage.getNumber(),
                    reviewPage.getSize(),
                    reviewPage.getTotalElements()
            );
            return ResponseEntity.ok(ApiResponse.success("Reviews retrieved successfully", paginationResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve reviews: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Review>> getReviewById(@PathVariable Long id) {
        try {
            Review review = reviewService.getReviewById(id);
            return ResponseEntity.ok(ApiResponse.success("Review retrieved successfully", review));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve review: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Review>> createReview(@Valid @RequestBody Review review) {
        try {
            Review createdReview = reviewService.createReview(review);
            return ResponseEntity.ok(ApiResponse.success("Review created successfully", createdReview));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create review: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Review>> updateReview(@PathVariable Long id, @Valid @RequestBody Review review) {
        try {
            Review updatedReview = reviewService.updateReview(id, review);
            return ResponseEntity.ok(ApiResponse.success("Review updated successfully", updatedReview));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update review: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok(ApiResponse.success("Review deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete review: " + e.getMessage()));
        }
    }
}

