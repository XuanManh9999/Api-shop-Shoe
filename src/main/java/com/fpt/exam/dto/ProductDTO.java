package com.fpt.exam.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long productId;
    private String name;
    private String description;
    private String brand;
    private String categoryName;
    private List<String> imageUrls;
    private List<VariantDTO> variants;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VariantDTO {
        private Long variantId;
        private String size;
        private String color;
        private Double price;
        private Double discountPrice;
        private Integer stock;
    }
}
