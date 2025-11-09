package com.fpt.exam.dto;

import lombok.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long orderId;
    private Date orderDate;
    private Double totalAmount;
    private String status;
    private String paymentMethod;
    private String shippingAddress;
    private String userEmail;
    private String discountCode;
    private List<OrderItemDTO> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDTO {
        private Long orderItemId;
        private String productName;
        private String size;
        private String color;
        private Integer quantity;
        private Double unitPrice;
        private Double totalPrice;
    }
}
