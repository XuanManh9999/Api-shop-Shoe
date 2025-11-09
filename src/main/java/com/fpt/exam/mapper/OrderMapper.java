package com.fpt.exam.mapper;

import com.fpt.exam.dto.OrderDTO;
import com.fpt.exam.entity.Order;

import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDTO toDTO(Order order) {
        if (order == null) return null;

        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .shippingAddress(order.getShippingAddress())
                .userEmail(order.getUser() != null ? order.getUser().getEmail() : null)
                .discountCode(order.getDiscount() != null ? order.getDiscount().getCode() : null)
                .items(order.getItems() != null
                        ? order.getItems().stream()
                        .map(i -> new OrderDTO.OrderItemDTO(
                                i.getOrderItemId(),
                                i.getVariant().getProduct().getName(),
                                i.getVariant().getSize(),
                                i.getVariant().getColor(),
                                i.getQuantity(),
                                i.getUnitPrice(),
                                i.getTotalPrice()))
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}

