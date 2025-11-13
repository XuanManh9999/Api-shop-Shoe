package com.fpt.exam.service.impl;

import com.fpt.exam.entity.Category;
import com.fpt.exam.exception.ResourceNotFoundException;
import com.fpt.exam.repository.CategoryRepository;
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
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy danh sách category có phân trang và tìm kiếm
    public Page<Category> getAllCategories(String keyword, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            return categoryRepository.searchCategories(keyword, pageable);
        }
        return categoryRepository.findAll(pageable);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category newCategory) {
        Category category = getCategoryById(id);
        category.setName(newCategory.getName());
        category.setDescription(newCategory.getDescription());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}

