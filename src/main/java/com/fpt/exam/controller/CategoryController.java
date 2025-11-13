package com.fpt.exam.controller;

import com.fpt.exam.dto.ApiResponse;
import com.fpt.exam.dto.PaginationResponse;
import com.fpt.exam.entity.Category;
import com.fpt.exam.service.impl.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<Category>>> getAllCategories(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "categoryId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<Category> categoryPage = categoryService.getAllCategories(keyword, page, size, sortBy, sortDir);
            PaginationResponse<Category> paginationResponse = PaginationResponse.of(
                    categoryPage.getContent(),
                    categoryPage.getNumber(),
                    categoryPage.getSize(),
                    categoryPage.getTotalElements()
            );
            return ResponseEntity.ok(ApiResponse.success("Categories retrieved successfully", paginationResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve categories: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(ApiResponse.success("Category retrieved successfully", category));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve category: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody Category category) {
        try {
            Category createdCategory = categoryService.createCategory(category);
            return ResponseEntity.ok(ApiResponse.success("Category created successfully", createdCategory));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create category: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable Long id, @Valid @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(ApiResponse.success("Category updated successfully", updatedCategory));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update category: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete category: " + e.getMessage()));
        }
    }
}

