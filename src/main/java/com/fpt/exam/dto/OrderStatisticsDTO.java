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
public class OrderStatisticsDTO {
    private String period; // "day", "month", "year"
    private String startDate;
    private String endDate;
    private Long totalOrders;
    private Double growthRate; // Tỷ lệ tăng trưởng so với period trước
    private List<OrderDataPoint> dataPoints;
    private Map<String, Long> ordersByStatus;
    private Map<String, Long> ordersByPaymentMethod;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderDataPoint {
        private String date; // Format depends on period
        private Long orderCount;
        private Double totalRevenue;
    }
}

