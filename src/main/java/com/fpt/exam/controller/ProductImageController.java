package com.fpt.exam.controller;

import com.fpt.exam.dto.ApiResponse;
import com.fpt.exam.dto.PaginationResponse;
import com.fpt.exam.entity.ProductImage;
import com.fpt.exam.service.impl.ProductImageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {

    @Autowired
    private ProductImageService imageService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<ProductImage>>> getAllImages(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Boolean isMain,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "imageId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<ProductImage> imagePage = imageService.getAllImages(keyword, productId, isMain, page, size, sortBy, sortDir);
            PaginationResponse<ProductImage> paginationResponse = PaginationResponse.of(
                    imagePage.getContent(),
                    imagePage.getNumber(),
                    imagePage.getSize(),
                    imagePage.getTotalElements()
            );
            return ResponseEntity.ok(ApiResponse.success("Product images retrieved successfully", paginationResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve product images: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductImage>> getImageById(@PathVariable Long id) {
        try {
            ProductImage image = imageService.getImageById(id);
            return ResponseEntity.ok(ApiResponse.success("Product image retrieved successfully", image));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve product image: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductImage>> createImage(@Valid @RequestBody ProductImage image) {
        try {
            ProductImage createdImage = imageService.addImage(image);
            return ResponseEntity.ok(ApiResponse.success("Product image created successfully", createdImage));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create product image: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductImage>> updateImage(@PathVariable Long id, @Valid @RequestBody ProductImage image) {
        try {
            ProductImage updatedImage = imageService.updateImage(id, image);
            return ResponseEntity.ok(ApiResponse.success("Product image updated successfully", updatedImage));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update product image: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteImage(@PathVariable Long id) {
        try {
            imageService.deleteImage(id);
            return ResponseEntity.ok(ApiResponse.success("Product image deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete product image: " + e.getMessage()));
        }
    }
}

