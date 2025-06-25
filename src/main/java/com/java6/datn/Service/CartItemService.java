package com.java6.datn.Service;

import com.java6.datn.DTO.CartItemDTO;

import java.util.List;

public interface CartItemService {
    List<CartItemDTO> getAllCartItems();
    List<CartItemDTO> getCartItemsByUser(Integer userID);
    CartItemDTO getCartItemById(Integer id);
    CartItemDTO createCartItem(CartItemDTO cartItemDTO);
    CartItemDTO updateCartItem(Integer id, CartItemDTO cartItemDTO);
    void deleteCartItem(Integer id);
}

