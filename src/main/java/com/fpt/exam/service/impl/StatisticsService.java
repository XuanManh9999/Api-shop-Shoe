package com.fpt.exam.service.impl;

import com.fpt.exam.dto.*;
import com.fpt.exam.repository.OrderItemRepository;
import com.fpt.exam.repository.OrderRepository;
import com.fpt.exam.repository.ProductRepository;
import com.fpt.exam.repository.ProductVariantRepository;
import com.fpt.exam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StatisticsService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private UserRepository userRepository;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("yyyy-MM");
    private static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy");

    public DashboardDTO getDashboardOverview() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();

        // Today
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date todayStart = cal.getTime();

        // This month
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date monthStart = cal.getTime();

        // This year
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date yearStart = cal.getTime();

        // All time
        Date allTimeStart = new Date(0);

        // Revenue Summary
        Double todayRevenue = orderRepository.getTotalRevenueByDateRange(todayStart, now);
        Double thisMonthRevenue = orderRepository.getTotalRevenueByDateRange(monthStart, now);
        Double thisYearRevenue = orderRepository.getTotalRevenueByDateRange(yearStart, now);
        Double totalRevenue = orderRepository.getTotalRevenueByDateRange(allTimeStart, now);

        DashboardDTO.RevenueSummary revenueSummary = DashboardDTO.RevenueSummary.builder()
                .todayRevenue(todayRevenue != null ? todayRevenue : 0.0)
                .thisMonthRevenue(thisMonthRevenue != null ? thisMonthRevenue : 0.0)
                .thisYearRevenue(thisYearRevenue != null ? thisYearRevenue : 0.0)
                .totalRevenue(totalRevenue != null ? totalRevenue : 0.0)
                .build();

        // Order Summary
        Long todayOrders = orderRepository.getOrderCountByDateRange(todayStart, now);
        Long thisMonthOrders = orderRepository.getOrderCountByDateRange(monthStart, now);
        Long thisYearOrders = orderRepository.getOrderCountByDateRange(yearStart, now);
        Long totalOrders = orderRepository.getOrderCountByDateRange(allTimeStart, now);

        DashboardDTO.OrderSummary orderSummary = DashboardDTO.OrderSummary.builder()
                .todayOrders(todayOrders != null ? todayOrders : 0L)
                .thisMonthOrders(thisMonthOrders != null ? thisMonthOrders : 0L)
                .thisYearOrders(thisYearOrders != null ? thisYearOrders : 0L)
                .totalOrders(totalOrders != null ? totalOrders : 0L)
                .build();

        // Total Users and Products
        Long totalUsers = userRepository.count();
        Long totalProducts = productRepository.count();

        // Orders by Status (last 30 days)
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, -30);
        Date last30Days = cal.getTime();
        List<Object[]> ordersByStatusList = orderRepository.getOrdersByStatus(last30Days, now);
        Map<String, Long> ordersByStatus = new HashMap<>();
        for (Object[] row : ordersByStatusList) {
            ordersByStatus.put((String) row[0], (Long) row[1]);
        }

        // Top 5 Selling Products (last 30 days)
        List<Object[]> topProductsData = orderItemRepository.getTopSellingProducts(last30Days, now);
        List<DashboardDTO.TopProductDTO> topSellingProducts = topProductsData.stream()
                .limit(5)
                .map(row -> DashboardDTO.TopProductDTO.builder()
                        .productId(((Number) row[0]).longValue())
                        .productName((String) row[1])
                        .totalSold(((Number) row[2]).longValue())
                        .totalRevenue(((Number) row[3]).doubleValue())
                        .build())
                .collect(Collectors.toList());

        // Monthly Revenue (last 12 months)
        cal.setTime(now);
        cal.add(Calendar.MONTH, -12);
        Date last12Months = cal.getTime();
        List<Object[]> monthlyRevenueData = orderRepository.getMonthlyRevenueStatistics(last12Months, now);
        List<DashboardDTO.MonthlyRevenueDTO> monthlyRevenue = monthlyRevenueData.stream()
                .map(row -> DashboardDTO.MonthlyRevenueDTO.builder()
                        .month((String) row[0])
                        .revenue(((Number) row[1]).doubleValue())
                        .orderCount(((Number) row[2]).longValue())
                        .build())
                .collect(Collectors.toList());

        return DashboardDTO.builder()
                .revenueSummary(revenueSummary)
                .orderSummary(orderSummary)
                .totalUsers(totalUsers)
                .totalProducts(totalProducts)
                .ordersByStatus(ordersByStatus)
                .topSellingProducts(topSellingProducts)
                .monthlyRevenue(monthlyRevenue)
                .build();
    }

    public RevenueStatisticsDTO getRevenueStatistics(String period, Date startDate, Date endDate) {
        if (endDate == null) {
            endDate = new Date();
        }
        if (startDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            if ("day".equals(period)) {
                cal.add(Calendar.DAY_OF_MONTH, -30);
            } else if ("month".equals(period)) {
                cal.add(Calendar.MONTH, -12);
            } else if ("year".equals(period)) {
                cal.add(Calendar.YEAR, -5);
            }
            startDate = cal.getTime();
        }

        List<Object[]> dataPoints;
        if ("day".equals(period)) {
            dataPoints = orderRepository.getDailyRevenueStatistics(startDate, endDate);
        } else if ("month".equals(period)) {
            dataPoints = orderRepository.getMonthlyRevenueStatistics(startDate, endDate);
        } else {
            dataPoints = orderRepository.getYearlyRevenueStatistics(startDate, endDate);
        }

        Double totalRevenue = orderRepository.getTotalRevenueByDateRange(startDate, endDate);
        Long totalOrders = orderRepository.getOrderCountByDateRange(startDate, endDate);
        Double averageOrderValue = totalOrders > 0 && totalRevenue != null ? totalRevenue / totalOrders : 0.0;

        List<RevenueStatisticsDTO.RevenueDataPoint> revenueDataPoints = dataPoints.stream()
                .map(row -> RevenueStatisticsDTO.RevenueDataPoint.builder()
                        .date((String) row[0])
                        .revenue(((Number) row[1]).doubleValue())
                        .orderCount(((Number) row[2]).longValue())
                        .build())
                .collect(Collectors.toList());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return RevenueStatisticsDTO.builder()
                .period(period)
                .startDate(sdf.format(startDate))
                .endDate(sdf.format(endDate))
                .totalRevenue(totalRevenue != null ? totalRevenue : 0.0)
                .totalOrders(totalOrders != null ? totalOrders : 0L)
                .averageOrderValue(averageOrderValue)
                .dataPoints(revenueDataPoints)
                .build();
    }

    public OrderStatisticsDTO getOrderStatistics(String period, Date startDate, Date endDate) {
        if (endDate == null) {
            endDate = new Date();
        }
        if (startDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            if ("day".equals(period)) {
                cal.add(Calendar.DAY_OF_MONTH, -30);
            } else if ("month".equals(period)) {
                cal.add(Calendar.MONTH, -12);
            } else if ("year".equals(period)) {
                cal.add(Calendar.YEAR, -5);
            }
            startDate = cal.getTime();
        }

        List<Object[]> dataPoints;
        if ("day".equals(period)) {
            dataPoints = orderRepository.getDailyRevenueStatistics(startDate, endDate);
        } else if ("month".equals(period)) {
            dataPoints = orderRepository.getMonthlyRevenueStatistics(startDate, endDate);
        } else {
            dataPoints = orderRepository.getYearlyRevenueStatistics(startDate, endDate);
        }

        Long totalOrders = orderRepository.getOrderCountByDateRange(startDate, endDate);

        // Calculate growth rate (compare with previous period)
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        long periodLength = endDate.getTime() - startDate.getTime();
        Date previousStartDate = new Date(startDate.getTime() - periodLength);
        Long previousPeriodOrders = orderRepository.getOrderCountByDateRange(previousStartDate, startDate);
        Double growthRate = previousPeriodOrders > 0 && totalOrders != null ?
                ((totalOrders - previousPeriodOrders) * 100.0 / previousPeriodOrders) : 0.0;

        List<OrderStatisticsDTO.OrderDataPoint> orderDataPoints = dataPoints.stream()
                .map(row -> OrderStatisticsDTO.OrderDataPoint.builder()
                        .date((String) row[0])
                        .orderCount(((Number) row[2]).longValue())
                        .totalRevenue(((Number) row[1]).doubleValue())
                        .build())
                .collect(Collectors.toList());

        // Orders by Status
        List<Object[]> ordersByStatusList = orderRepository.getOrdersByStatus(startDate, endDate);
        Map<String, Long> ordersByStatus = new HashMap<>();
        for (Object[] row : ordersByStatusList) {
            ordersByStatus.put((String) row[0], (Long) row[1]);
        }

        // Orders by Payment Method
        List<Object[]> ordersByPaymentList = orderRepository.getOrdersByPaymentMethod(startDate, endDate);
        Map<String, Long> ordersByPaymentMethod = new HashMap<>();
        for (Object[] row : ordersByPaymentList) {
            ordersByPaymentMethod.put((String) row[0], (Long) row[1]);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return OrderStatisticsDTO.builder()
                .period(period)
                .startDate(sdf.format(startDate))
                .endDate(sdf.format(endDate))
                .totalOrders(totalOrders != null ? totalOrders : 0L)
                .growthRate(growthRate)
                .dataPoints(orderDataPoints)
                .ordersByStatus(ordersByStatus)
                .ordersByPaymentMethod(ordersByPaymentMethod)
                .build();
    }

    public ProductStatisticsDTO getProductStatistics(String period) {
        Long totalProducts = productRepository.count();

        // Determine date range based on period
        Date endDate = new Date();
        Date startDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        if ("day".equals(period)) {
            cal.add(Calendar.DAY_OF_MONTH, -30);
        } else if ("month".equals(period)) {
            cal.add(Calendar.MONTH, -12);
        } else {
            cal.add(Calendar.YEAR, -5);
        }
        startDate = cal.getTime();

        // Top Selling Products
        List<Object[]> topProductsData = orderItemRepository.getTopSellingProducts(startDate, endDate);
        List<ProductStatisticsDTO.TopSellingProductDTO> topSellingProducts = topProductsData.stream()
                .limit(10)
                .map(row -> {
                    String categoryName = row.length > 4 && row[4] != null ? (String) row[4] : null;
                    return ProductStatisticsDTO.TopSellingProductDTO.builder()
                            .productId(((Number) row[0]).longValue())
                            .productName((String) row[1])
                            .categoryName(categoryName)
                            .totalSold(((Number) row[2]).longValue())
                            .totalRevenue(((Number) row[3]).doubleValue())
                            .build();
                })
                .collect(Collectors.toList());

        // Low Stock Products
        List<Object[]> lowStockData = productVariantRepository.findLowStockVariants(10);
        List<ProductStatisticsDTO.LowStockProductDTO> lowStockProducts = lowStockData.stream()
                .map(row -> {
                    String variantInfo = "";
                    if (row[2] != null) variantInfo += "Size: " + row[2];
                    if (row[3] != null) {
                        if (!variantInfo.isEmpty()) variantInfo += ", ";
                        variantInfo += "Color: " + row[3];
                    }
                    return ProductStatisticsDTO.LowStockProductDTO.builder()
                            .productId(((Number) row[0]).longValue())
                            .productName((String) row[1])
                            .variantInfo(variantInfo)
                            .currentStock(((Number) row[4]).intValue())
                            .minStockThreshold(10)
                            .build();
                })
                .collect(Collectors.toList());

        // Products by Category
        List<Object[]> productsByCategoryData = productRepository.getProductCountByCategory();
        Map<String, Long> productsByCategory = new HashMap<>();
        for (Object[] row : productsByCategoryData) {
            productsByCategory.put((String) row[1], ((Number) row[2]).longValue());
        }

        return ProductStatisticsDTO.builder()
                .period(period)
                .totalProducts(totalProducts)
                .topSellingProducts(topSellingProducts)
                .lowStockProducts(lowStockProducts)
                .productsByCategory(productsByCategory)
                .build();
    }

    public UserStatisticsDTO getUserStatistics(String period) {
        Long totalUsers = userRepository.count();

        // Determine date range based on period
        Date endDate = new Date();
        Date startDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        if ("day".equals(period)) {
            cal.add(Calendar.DAY_OF_MONTH, -30);
        } else if ("month".equals(period)) {
            cal.add(Calendar.MONTH, -12);
        } else {
            cal.add(Calendar.YEAR, -5);
        }
        startDate = cal.getTime();

        // Active Users (users with orders in period)
        Long activeUsers = userRepository.countActiveUsers(startDate, endDate);

        // New Users by Period
        List<Object[]> userDataPoints;
        if ("day".equals(period)) {
            userDataPoints = userRepository.getDailyUserStatistics(startDate, endDate);
        } else if ("month".equals(period)) {
            userDataPoints = userRepository.getMonthlyUserStatistics(startDate, endDate);
        } else {
            userDataPoints = userRepository.getYearlyUserStatistics(startDate, endDate);
        }

        List<UserStatisticsDTO.UserDataPoint> newUsersByPeriod = userDataPoints.stream()
                .map(row -> UserStatisticsDTO.UserDataPoint.builder()
                        .date((String) row[0])
                        .newUserCount(((Number) row[1]).longValue())
                        .build())
                .collect(Collectors.toList());

        // Users by Role
        List<Object[]> usersByRoleData = userRepository.getUsersByRole();
        Map<String, Long> usersByRole = new HashMap<>();
        for (Object[] row : usersByRoleData) {
            usersByRole.put((String) row[0], (Long) row[1]);
        }

        return UserStatisticsDTO.builder()
                .period(period)
                .totalUsers(totalUsers)
                .activeUsers(activeUsers != null ? activeUsers : 0L)
                .newUsersByPeriod(newUsersByPeriod)
                .usersByRole(usersByRole)
                .build();
    }

    public CategoryStatisticsDTO getCategoryStatistics() {
        Date endDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        cal.add(Calendar.YEAR, -1);
        Date startDate = cal.getTime();

        // Get category statistics from order items
        List<Object[]> categoryDataList = orderItemRepository.getCategoryStatistics(startDate, endDate);

        // Get product count by category
        List<Object[]> productCountByCategory = productRepository.getProductCountByCategory();
        Map<Long, Long> productCountMap = new HashMap<>();
        for (Object[] row : productCountByCategory) {
            productCountMap.put(((Number) row[0]).longValue(), ((Number) row[2]).longValue());
        }

        List<CategoryStatisticsDTO.CategoryData> categoryData = categoryDataList.stream()
                .map(row -> {
                    Long categoryId = ((Number) row[0]).longValue();
                    Long productCount = productCountMap.getOrDefault(categoryId, 0L);
                    Long orderCount = ((Number) row[2]).longValue();
                    Double totalRevenue = ((Number) row[3]).doubleValue();
                    Double averageOrderValue = orderCount > 0 ? totalRevenue / orderCount : 0.0;

                    return CategoryStatisticsDTO.CategoryData.builder()
                            .categoryId(categoryId)
                            .categoryName((String) row[1])
                            .productCount(productCount)
                            .orderCount(orderCount)
                            .totalRevenue(totalRevenue)
                            .averageOrderValue(averageOrderValue)
                            .build();
                })
                .collect(Collectors.toList());

        return CategoryStatisticsDTO.builder()
                .categoryData(categoryData)
                .build();
    }

    public List<ProductStatisticsDTO.TopSellingProductDTO> getTopSellingProducts(int limit, String period) {
        Date endDate = new Date();
        Date startDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        if ("day".equals(period)) {
            cal.add(Calendar.DAY_OF_MONTH, -30);
        } else if ("month".equals(period)) {
            cal.add(Calendar.MONTH, -12);
        } else {
            cal.add(Calendar.YEAR, -5);
        }
        startDate = cal.getTime();

        List<Object[]> topProductsData = orderItemRepository.getTopSellingProducts(startDate, endDate);
        return topProductsData.stream()
                .limit(limit)
                .map(row -> {
                    String categoryName = row.length > 4 && row[4] != null ? (String) row[4] : null;
                    return ProductStatisticsDTO.TopSellingProductDTO.builder()
                            .productId(((Number) row[0]).longValue())
                            .productName((String) row[1])
                            .categoryName(categoryName)
                            .totalSold(((Number) row[2]).longValue())
                            .totalRevenue(((Number) row[3]).doubleValue())
                            .build();
                })
                .collect(Collectors.toList());
    }
}

