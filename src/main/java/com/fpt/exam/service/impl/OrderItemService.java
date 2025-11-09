package com.fpt.exam.service.impl;

import com.fpt.exam.entity.Order;
import com.fpt.exam.entity.OrderItem;
import com.fpt.exam.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<OrderItem> getItemsByOrder(Order order) {
        return orderItemRepository.findByOrder(order);
    }

    public OrderItem addItem(OrderItem item) {
        return orderItemRepository.save(item);
    }

    public void deleteItem(Long id) {
        orderItemRepository.deleteById(id);
    }
}

