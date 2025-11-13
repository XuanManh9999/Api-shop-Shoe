package com.fpt.exam.service.impl;

import com.fpt.exam.entity.Order;
import com.fpt.exam.entity.User;
import com.fpt.exam.exception.ResourceNotFoundException;
import com.fpt.exam.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    // Lấy danh sách order có phân trang và tìm kiếm
    public Page<Order> getAllOrders(String keyword, String status, Long userId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if ((keyword != null && !keyword.trim().isEmpty()) || status != null || userId != null) {
            return orderRepository.searchOrders(keyword, status, userId, pageable);
        }
        return orderRepository.findAll(pageable);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order newOrder) {
        Order order = getOrderById(id);
        order.setStatus(newOrder.getStatus());
        order.setPaymentMethod(newOrder.getPaymentMethod());
        order.setShippingAddress(newOrder.getShippingAddress());
        order.setTotalAmount(newOrder.getTotalAmount());
        if (newOrder.getDiscount() != null) {
            order.setDiscount(newOrder.getDiscount());
        }
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }
}

