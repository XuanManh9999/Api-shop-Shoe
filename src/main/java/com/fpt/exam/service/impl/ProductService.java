package com.fpt.exam.service.impl;

import com.fpt.exam.entity.Category;
import com.fpt.exam.entity.Product;
import com.fpt.exam.exception.ResourceNotFoundException;
import com.fpt.exam.repository.ProductRepository;
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
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // Lấy danh sách product có phân trang và tìm kiếm
    public Page<Product> getAllProducts(String keyword, Long categoryId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if ((keyword != null && !keyword.trim().isEmpty()) || categoryId != null) {
            return productRepository.searchProducts(keyword, categoryId, pageable);
        }
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product newProduct) {
        Product product = getProductById(id);
        product.setName(newProduct.getName());
        product.setDescription(newProduct.getDescription());
        product.setBrand(newProduct.getBrand());
        if (newProduct.getCategory() != null) {
            product.setCategory(newProduct.getCategory());
        }
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }
}

