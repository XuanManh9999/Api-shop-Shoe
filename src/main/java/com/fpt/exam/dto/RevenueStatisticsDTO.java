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
public class RevenueStatisticsDTO {
    private String period; // "day", "month", "year"
    private String startDate;
    private String endDate;
    private Double totalRevenue;
    private Long totalOrders;
    private Double averageOrderValue;
    private List<RevenueDataPoint> dataPoints;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RevenueDataPoint {
        private String date; // Format depends on period: "YYYY-MM-DD", "YYYY-MM", or "YYYY"
        private Double revenue;
        private Long orderCount;
    }
}

