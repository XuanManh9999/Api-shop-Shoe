package com.fpt.exam.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    private Long cartItemId;
    private Long variantId;
    private String productName;
    private String size;
    private String color;
    private Integer quantity;
    private Double totalPrice;
}
