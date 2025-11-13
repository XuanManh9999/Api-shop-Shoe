package com.fpt.exam.repository;

import com.fpt.exam.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);
    
    // Statistics queries
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate AND u.createdAt < :endDate")
    Long countUsersByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query(value = "SELECT DATE_FORMAT(u.created_at, '%Y-%m-%d') as date, COUNT(u.user_id) as userCount " +
           "FROM `user` u " +
           "WHERE u.created_at >= :startDate AND u.created_at < :endDate " +
           "GROUP BY DATE_FORMAT(u.created_at, '%Y-%m-%d') " +
           "ORDER BY date ASC", nativeQuery = true)
    List<Object[]> getDailyUserStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query(value = "SELECT DATE_FORMAT(u.created_at, '%Y-%m') as date, COUNT(u.user_id) as userCount " +
           "FROM `user` u " +
           "WHERE u.created_at >= :startDate AND u.created_at < :endDate " +
           "GROUP BY DATE_FORMAT(u.created_at, '%Y-%m') " +
           "ORDER BY date ASC", nativeQuery = true)
    List<Object[]> getMonthlyUserStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query(value = "SELECT DATE_FORMAT(u.created_at, '%Y') as date, COUNT(u.user_id) as userCount " +
           "FROM `user` u " +
           "WHERE u.created_at >= :startDate AND u.created_at < :endDate " +
           "GROUP BY DATE_FORMAT(u.created_at, '%Y') " +
           "ORDER BY date ASC", nativeQuery = true)
    List<Object[]> getYearlyUserStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT u.role.roleName, COUNT(u) FROM User u WHERE u.role IS NOT NULL GROUP BY u.role.roleName")
    List<Object[]> getUsersByRole();
    
    @Query("SELECT COUNT(DISTINCT o.user.userId) FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate < :endDate")
    Long countActiveUsers(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}

