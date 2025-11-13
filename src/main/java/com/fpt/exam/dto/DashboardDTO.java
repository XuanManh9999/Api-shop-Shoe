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
public class DashboardDTO {
    // Tổng quan
    private RevenueSummary revenueSummary;
    private OrderSummary orderSummary;
    private Long totalUsers;
    private Long totalProducts;
    
    // Đơn hàng theo trạng thái
    private Map<String, Long> ordersByStatus;
    
    // Top sản phẩm bán chạy
    private List<TopProductDTO> topSellingProducts;
    
    // Doanh thu theo tháng (12 tháng gần nhất)
    private List<MonthlyRevenueDTO> monthlyRevenue;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RevenueSummary {
        private Double todayRevenue;
        private Double thisMonthRevenue;
        private Double thisYearRevenue;
        private Double totalRevenue;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderSummary {
        private Long todayOrders;
        private Long thisMonthOrders;
        private Long thisYearOrders;
        private Long totalOrders;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopProductDTO {
        private Long productId;
        private String productName;
        private Long totalSold;
        private Double totalRevenue;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MonthlyRevenueDTO {
        private String month; // Format: "YYYY-MM"
        private Double revenue;
        private Long orderCount;
    }
}

