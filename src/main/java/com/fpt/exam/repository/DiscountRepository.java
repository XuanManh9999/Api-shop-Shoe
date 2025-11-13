package com.fpt.exam.repository;

import com.fpt.exam.entity.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByCode(String code);
    
    @Query("SELECT d FROM Discount d WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(d.code) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Discount> searchDiscounts(@Param("keyword") String keyword, Pageable pageable);
}

