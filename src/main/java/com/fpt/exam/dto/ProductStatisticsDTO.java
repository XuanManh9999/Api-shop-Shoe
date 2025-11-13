package com.fpt.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStatisticsDTO {
    private String period; // "day", "month", "year"
    private Long totalProducts;
    private List<TopSellingProductDTO> topSellingProducts;
    private List<LowStockProductDTO> lowStockProducts;
    private Map<String, Long> productsByCategory;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopSellingProductDTO {
        private Long productId;
        private String productName;
        private String categoryName;
        private Long totalSold;
        private Double totalRevenue;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LowStockProductDTO {
        private Long productId;
        private String productName;
        private String variantInfo; // size, color
        private Integer currentStock;
        private Integer minStockThreshold; // Có thể set default là 10
    }
}

