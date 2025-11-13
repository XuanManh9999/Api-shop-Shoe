package com.fpt.exam.controller;

import com.fpt.exam.dto.ApiResponse;
import com.fpt.exam.dto.PaginationResponse;
import com.fpt.exam.entity.Discount;
import com.fpt.exam.service.impl.DiscountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<Discount>>> getAllDiscounts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "discountId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<Discount> discountPage = discountService.getAllDiscounts(keyword, page, size, sortBy, sortDir);
            PaginationResponse<Discount> paginationResponse = PaginationResponse.of(
                    discountPage.getContent(),
                    discountPage.getNumber(),
                    discountPage.getSize(),
                    discountPage.getTotalElements()
            );
            return ResponseEntity.ok(ApiResponse.success("Discounts retrieved successfully", paginationResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve discounts: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Discount>> getDiscountById(@PathVariable Long id) {
        try {
            Discount discount = discountService.getDiscountById(id);
            return ResponseEntity.ok(ApiResponse.success("Discount retrieved successfully", discount));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve discount: " + e.getMessage()));
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<Discount>> getDiscountByCode(@PathVariable String code) {
        try {
            return discountService.getDiscountByCode(code)
                    .map(discount -> ResponseEntity.ok(ApiResponse.success("Discount retrieved successfully", discount)))
                    .orElse(ResponseEntity.badRequest()
                            .body(ApiResponse.error("Discount not found with code: " + code)));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve discount: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Discount>> createDiscount(@Valid @RequestBody Discount discount) {
        try {
            Discount createdDiscount = discountService.createDiscount(discount);
            return ResponseEntity.ok(ApiResponse.success("Discount created successfully", createdDiscount));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create discount: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Discount>> updateDiscount(@PathVariable Long id, @Valid @RequestBody Discount discount) {
        try {
            Discount updatedDiscount = discountService.updateDiscount(id, discount);
            return ResponseEntity.ok(ApiResponse.success("Discount updated successfully", updatedDiscount));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update discount: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDiscount(@PathVariable Long id) {
        try {
            discountService.deleteDiscount(id);
            return ResponseEntity.ok(ApiResponse.success("Discount deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete discount: " + e.getMessage()));
        }
    }
}

