package com.fpt.exam.controller;

import com.fpt.exam.dto.*;
import com.fpt.exam.service.impl.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardDTO>> getDashboard() {
        try {
            DashboardDTO dashboard = statisticsService.getDashboardOverview();
            return ResponseEntity.ok(ApiResponse.success("Dashboard data retrieved successfully", dashboard));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve dashboard data: " + e.getMessage()));
        }
    }

    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse<RevenueStatisticsDTO>> getRevenueStatistics(
            @RequestParam(required = false, defaultValue = "month") String period,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            // Validate period
            if (!period.equals("day") && !period.equals("month") && !period.equals("year")) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid period. Must be 'day', 'month', or 'year'"));
            }

            RevenueStatisticsDTO statistics = statisticsService.getRevenueStatistics(period, startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success("Revenue statistics retrieved successfully", statistics));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve revenue statistics: " + e.getMessage()));
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<OrderStatisticsDTO>> getOrderStatistics(
            @RequestParam(required = false, defaultValue = "month") String period,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            // Validate period
            if (!period.equals("day") && !period.equals("month") && !period.equals("year")) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid period. Must be 'day', 'month', or 'year'"));
            }

            OrderStatisticsDTO statistics = statisticsService.getOrderStatistics(period, startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success("Order statistics retrieved successfully", statistics));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve order statistics: " + e.getMessage()));
        }
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<ProductStatisticsDTO>> getProductStatistics(
            @RequestParam(required = false, defaultValue = "month") String period) {
        try {
            // Validate period
            if (!period.equals("day") && !period.equals("month") && !period.equals("year")) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid period. Must be 'day', 'month', or 'year'"));
            }

            ProductStatisticsDTO statistics = statisticsService.getProductStatistics(period);
            return ResponseEntity.ok(ApiResponse.success("Product statistics retrieved successfully", statistics));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve product statistics: " + e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<UserStatisticsDTO>> getUserStatistics(
            @RequestParam(required = false, defaultValue = "month") String period) {
        try {
            // Validate period
            if (!period.equals("day") && !period.equals("month") && !period.equals("year")) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid period. Must be 'day', 'month', or 'year'"));
            }

            UserStatisticsDTO statistics = statisticsService.getUserStatistics(period);
            return ResponseEntity.ok(ApiResponse.success("User statistics retrieved successfully", statistics));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve user statistics: " + e.getMessage()));
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryStatisticsDTO>> getCategoryStatistics() {
        try {
            CategoryStatisticsDTO statistics = statisticsService.getCategoryStatistics();
            return ResponseEntity.ok(ApiResponse.success("Category statistics retrieved successfully", statistics));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve category statistics: " + e.getMessage()));
        }
    }

    @GetMapping("/top-products")
    public ResponseEntity<ApiResponse<List<ProductStatisticsDTO.TopSellingProductDTO>>> getTopSellingProducts(
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "month") String period) {
        try {
            // Validate period
            if (!period.equals("day") && !period.equals("month") && !period.equals("year")) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid period. Must be 'day', 'month', or 'year'"));
            }

            // Validate limit
            if (limit < 1 || limit > 100) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Limit must be between 1 and 100"));
            }

            List<ProductStatisticsDTO.TopSellingProductDTO> topProducts = 
                    statisticsService.getTopSellingProducts(limit, period);
            return ResponseEntity.ok(ApiResponse.success("Top selling products retrieved successfully", topProducts));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve top selling products: " + e.getMessage()));
        }
    }
}

