package com.fpt.exam.repository;

import com.fpt.exam.entity.Order;
import com.fpt.exam.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
    
    // Statistics queries for product sales
    @Query("SELECT oi.variant.product.productId, oi.variant.product.name, " +
           "SUM(oi.quantity) as totalSold, SUM(oi.totalPrice) as totalRevenue, " +
           "oi.variant.product.category.name " +
           "FROM OrderItem oi " +
           "WHERE oi.order.orderDate >= :startDate AND oi.order.orderDate < :endDate " +
           "GROUP BY oi.variant.product.productId, oi.variant.product.name, oi.variant.product.category.name " +
           "ORDER BY totalSold DESC")
    List<Object[]> getTopSellingProducts(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT oi.variant.product.category.categoryId, oi.variant.product.category.name, " +
           "COUNT(DISTINCT oi.order.orderId) as orderCount, " +
           "SUM(oi.totalPrice) as totalRevenue " +
           "FROM OrderItem oi " +
           "WHERE oi.order.orderDate >= :startDate AND oi.order.orderDate < :endDate " +
           "AND oi.variant.product.category IS NOT NULL " +
           "GROUP BY oi.variant.product.category.categoryId, oi.variant.product.category.name")
    List<Object[]> getCategoryStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
}

