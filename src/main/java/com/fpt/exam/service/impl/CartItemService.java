package com.fpt.exam.service.impl;

import com.fpt.exam.entity.Cart;
import com.fpt.exam.entity.CartItem;
import com.fpt.exam.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    public List<CartItem> getItemsByCart(Cart cart) {
        return cartItemRepository.findByCart(cart);
    }

    public CartItem addItem(CartItem item) {
        return cartItemRepository.save(item);
    }

    public void deleteItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    public void clearCart(Cart cart) {
        cartItemRepository.deleteByCart(cart);
    }
}

