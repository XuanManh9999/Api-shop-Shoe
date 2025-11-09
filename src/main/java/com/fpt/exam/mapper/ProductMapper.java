package com.fpt.exam.mapper;

import com.fpt.exam.dto.ProductDTO;
import com.fpt.exam.entity.Product;
import com.fpt.exam.entity.ProductImage;

import java.util.stream.Collectors;

public class ProductMapper {
    public static ProductDTO toDTO(Product product) {
        if (product == null) return null;

        return ProductDTO.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .brand(product.getBrand())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .imageUrls(product.getImages() != null
                        ? product.getImages().stream().map(ProductImage::getImageUrl).collect(Collectors.toList())
                        : null)
                .variants(product.getVariants() != null
                        ? product.getVariants().stream()
                        .map(v -> new ProductDTO.VariantDTO(
                                v.getVariantId(), v.getSize(), v.getColor(), v.getPrice(), v.getDiscountPrice(), v.getStock()))
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}
