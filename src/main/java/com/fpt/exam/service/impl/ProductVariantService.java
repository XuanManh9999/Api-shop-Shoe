package com.fpt.exam.service.impl;

import com.fpt.exam.entity.ProductVariant;
import com.fpt.exam.exception.ResourceNotFoundException;
import com.fpt.exam.repository.ProductVariantRepository;
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
public class ProductVariantService {
    @Autowired
    private ProductVariantRepository variantRepository;

    // Lấy danh sách variant có phân trang và tìm kiếm
    public Page<ProductVariant> getAllVariants(String keyword, Long productId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if ((keyword != null && !keyword.trim().isEmpty()) || productId != null) {
            return variantRepository.searchVariants(keyword, productId, pageable);
        }
        return variantRepository.findAll(pageable);
    }

    public ProductVariant getVariantById(Long id) {
        return variantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductVariant not found with id: " + id));
    }

    public ProductVariant createVariant(ProductVariant variant) {
        return variantRepository.save(variant);
    }

    public ProductVariant updateVariant(Long id, ProductVariant newVariant) {
        ProductVariant variant = getVariantById(id);
        variant.setSize(newVariant.getSize());
        variant.setColor(newVariant.getColor());
        variant.setPrice(newVariant.getPrice());
        variant.setDiscountPrice(newVariant.getDiscountPrice());
        variant.setStock(newVariant.getStock());
        if (newVariant.getProduct() != null) {
            variant.setProduct(newVariant.getProduct());
        }
        return variantRepository.save(variant);
    }

    public void deleteVariant(Long id) {
        ProductVariant variant = getVariantById(id);
        variantRepository.delete(variant);
    }
}

