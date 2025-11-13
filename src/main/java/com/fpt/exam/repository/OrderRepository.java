package com.fpt.exam.repository;

import com.fpt.exam.entity.Order;
import com.fpt.exam.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByStatus(String status);
    
    @Query("SELECT o FROM Order o WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(o.status) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(o.paymentMethod) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(o.shippingAddress) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(o.user.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(o.user.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:userId IS NULL OR o.user.userId = :userId)")
    Page<Order> searchOrders(@Param("keyword") String keyword, 
                             @Param("status") String status,
                             @Param("userId") Long userId, 
                             Pageable pageable);
    
    // Statistics queries
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate < :endDate")
    Double getTotalRevenueByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate < :endDate")
    Long getOrderCountByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query(value = "SELECT DATE_FORMAT(o.order_date, '%Y-%m-%d') as date, " +
           "COALESCE(SUM(o.total_amount), 0) as revenue, " +
           "COUNT(o.order_id) as orderCount " +
           "FROM `order` o " +
           "WHERE o.order_date >= :startDate AND o.order_date < :endDate " +
           "GROUP BY DATE_FORMAT(o.order_date, '%Y-%m-%d') " +
           "ORDER BY date ASC", nativeQuery = true)
    List<Object[]> getDailyRevenueStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query(value = "SELECT DATE_FORMAT(o.order_date, '%Y-%m') as date, " +
           "COALESCE(SUM(o.total_amount), 0) as revenue, " +
           "COUNT(o.order_id) as orderCount " +
           "FROM `order` o " +
           "WHERE o.order_date >= :startDate AND o.order_date < :endDate " +
           "GROUP BY DATE_FORMAT(o.order_date, '%Y-%m') " +
           "ORDER BY date ASC", nativeQuery = true)
    List<Object[]> getMonthlyRevenueStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query(value = "SELECT DATE_FORMAT(o.order_date, '%Y') as date, " +
           "COALESCE(SUM(o.total_amount), 0) as revenue, " +
           "COUNT(o.order_id) as orderCount " +
           "FROM `order` o " +
           "WHERE o.order_date >= :startDate AND o.order_date < :endDate " +
           "GROUP BY DATE_FORMAT(o.order_date, '%Y') " +
           "ORDER BY date ASC", nativeQuery = true)
    List<Object[]> getYearlyRevenueStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT o.status, COUNT(o) FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate < :endDate GROUP BY o.status")
    List<Object[]> getOrdersByStatus(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT o.paymentMethod, COUNT(o) FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate < :endDate AND o.paymentMethod IS NOT NULL GROUP BY o.paymentMethod")
    List<Object[]> getOrdersByPaymentMethod(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate < :endDate")
    Long countOrdersByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
