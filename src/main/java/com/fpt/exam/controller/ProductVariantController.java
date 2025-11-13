package com.fpt.exam.controller;

import com.fpt.exam.dto.ApiResponse;
import com.fpt.exam.dto.PaginationResponse;
import com.fpt.exam.entity.ProductVariant;
import com.fpt.exam.service.impl.ProductVariantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/variants")
public class ProductVariantController {

    @Autowired
    private ProductVariantService variantService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<ProductVariant>>> getAllVariants(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "variantId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<ProductVariant> variantPage = variantService.getAllVariants(keyword, productId, page, size, sortBy, sortDir);
            PaginationResponse<ProductVariant> paginationResponse = PaginationResponse.of(
                    variantPage.getContent(),
                    variantPage.getNumber(),
                    variantPage.getSize(),
                    variantPage.getTotalElements()
            );
            return ResponseEntity.ok(ApiResponse.success("Product variants retrieved successfully", paginationResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve product variants: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductVariant>> getVariantById(@PathVariable Long id) {
        try {
            ProductVariant variant = variantService.getVariantById(id);
            return ResponseEntity.ok(ApiResponse.success("Product variant retrieved successfully", variant));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve product variant: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductVariant>> createVariant(@Valid @RequestBody ProductVariant variant) {
        try {
            ProductVariant createdVariant = variantService.createVariant(variant);
            return ResponseEntity.ok(ApiResponse.success("Product variant created successfully", createdVariant));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create product variant: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductVariant>> updateVariant(@PathVariable Long id, @Valid @RequestBody ProductVariant variant) {
        try {
            ProductVariant updatedVariant = variantService.updateVariant(id, variant);
            return ResponseEntity.ok(ApiResponse.success("Product variant updated successfully", updatedVariant));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update product variant: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVariant(@PathVariable Long id) {
        try {
            variantService.deleteVariant(id);
            return ResponseEntity.ok(ApiResponse.success("Product variant deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete product variant: " + e.getMessage()));
        }
    }
}
