package com.fpt.exam.service.impl;

import com.fpt.exam.entity.ProductVariant;
import com.fpt.exam.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductVariantService {
    @Autowired
    private ProductVariantRepository variantRepository;

    public List<ProductVariant> getAllVariants() {
        return variantRepository.findAll();
    }

    public Optional<ProductVariant> getVariantById(Long id) {
        return variantRepository.findById(id);
    }

    public ProductVariant createVariant(ProductVariant variant) {
        return variantRepository.save(variant);
    }

    public Optional<ProductVariant> updateVariant(Long id, ProductVariant newVariant) {
        return variantRepository.findById(id).map(v -> {
            v.setSize(newVariant.getSize());
            v.setColor(newVariant.getColor());
            v.setPrice(newVariant.getPrice());
            v.setDiscountPrice(newVariant.getDiscountPrice());
            v.setStock(newVariant.getStock());
            return variantRepository.save(v);
        });
    }

    public void deleteVariant(Long id) {
        variantRepository.deleteById(id);
    }
}

