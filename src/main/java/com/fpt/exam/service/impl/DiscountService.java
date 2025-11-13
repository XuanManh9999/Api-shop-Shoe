package com.fpt.exam.service.impl;

import com.fpt.exam.entity.Discount;
import com.fpt.exam.exception.ResourceNotFoundException;
import com.fpt.exam.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class DiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    // Lấy danh sách discount có phân trang và tìm kiếm
    public Page<Discount> getAllDiscounts(String keyword, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            return discountRepository.searchDiscounts(keyword, pageable);
        }
        return discountRepository.findAll(pageable);
    }

    public Discount getDiscountById(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id: " + id));
    }

    public Optional<Discount> getDiscountByCode(String code) {
        return discountRepository.findByCode(code);
    }

    public Discount createDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    public Discount updateDiscount(Long id, Discount newDiscount) {
        Discount discount = getDiscountById(id);
        discount.setCode(newDiscount.getCode());
        discount.setDescription(newDiscount.getDescription());
        discount.setDiscountPercent(newDiscount.getDiscountPercent());
        discount.setStartDate(newDiscount.getStartDate());
        discount.setEndDate(newDiscount.getEndDate());
        return discountRepository.save(discount);
    }

    public void deleteDiscount(Long id) {
        Discount discount = getDiscountById(id);
        discountRepository.delete(discount);
    }
}

