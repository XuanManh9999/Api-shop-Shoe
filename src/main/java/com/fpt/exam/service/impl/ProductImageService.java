package com.fpt.exam.service.impl;

import com.fpt.exam.entity.Product;
import com.fpt.exam.entity.ProductImage;
import com.fpt.exam.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {
    @Autowired
    private ProductImageRepository imageRepository;

    public List<ProductImage> getImagesByProduct(Product product) {
        return imageRepository.findByProduct(product);
    }

    public ProductImage addImage(ProductImage image) {
        return imageRepository.save(image);
    }

    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
}
