package com.fpt.exam.mapper;


import com.fpt.exam.dto.CartItemDTO;
import com.fpt.exam.entity.CartItem;

public class CartItemMapper {
    public static CartItemDTO toDTO(CartItem item) {
        if (item == null) return null;

        return CartItemDTO.builder()
                .cartItemId(item.getCartItemId())
                .variantId(item.getVariant().getVariantId())
                .productName(item.getVariant().getProduct().getName())
                .size(item.getVariant().getSize())
                .color(item.getVariant().getColor())
                .quantity(item.getQuantity())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}

