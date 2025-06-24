package com.java6.datn.Service;

import com.java6.datn.DTO.CartItemDTO;

import java.util.List;

public interface CartItemService {
    List<CartItemDTO> getAllCartItems();
    List<CartItemDTO> getCartItemsByUser(Long userID);
    CartItemDTO getCartItemById(Long id);
    CartItemDTO createCartItem(CartItemDTO cartItemDTO);
    CartItemDTO updateCartItem(Long id, CartItemDTO cartItemDTO);
    void deleteCartItem(Long id);
}

