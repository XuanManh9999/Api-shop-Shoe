package com.fpt.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryStatisticsDTO {
    private List<CategoryData> categoryData;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryData {
        private Long categoryId;
        private String categoryName;
        private Long productCount;
        private Long orderCount;
        private Double totalRevenue;
        private Double averageOrderValue;
    }
}

