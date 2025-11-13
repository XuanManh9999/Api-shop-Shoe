package com.fpt.exam.service.impl;

import com.fpt.exam.entity.Product;
import com.fpt.exam.entity.ProductImage;
import com.fpt.exam.exception.ResourceNotFoundException;
import com.fpt.exam.repository.ProductImageRepository;
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
public class ProductImageService {
    @Autowired
    private ProductImageRepository imageRepository;

    // Lấy danh sách image có phân trang và tìm kiếm
    public Page<ProductImage> getAllImages(String keyword, Long productId, Boolean isMain, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if ((keyword != null && !keyword.trim().isEmpty()) || productId != null || isMain != null) {
            return imageRepository.searchImages(keyword, productId, isMain, pageable);
        }
        return imageRepository.findAll(pageable);
    }

    public ProductImage getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage not found with id: " + id));
    }

    public List<ProductImage> getImagesByProduct(Product product) {
        return imageRepository.findByProduct(product);
    }

    public ProductImage addImage(ProductImage image) {
        return imageRepository.save(image);
    }

    public ProductImage updateImage(Long id, ProductImage newImage) {
        ProductImage image = getImageById(id);
        image.setImageUrl(newImage.getImageUrl());
        image.setIsMain(newImage.getIsMain());
        if (newImage.getProduct() != null) {
            image.setProduct(newImage.getProduct());
        }
        return imageRepository.save(image);
    }

    public void deleteImage(Long id) {
        ProductImage image = getImageById(id);
        imageRepository.delete(image);
    }
}
