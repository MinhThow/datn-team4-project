package com.java6.datn.Service.Impl;

import com.java6.datn.Entity.CartItem;
import com.java6.datn.Repository.CartItemRepository;
import com.java6.datn.Service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService.CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public void clearCartByUserId(Integer userId) {
        List<CartItem> items = cartItemRepository.findByUserID(userId);
        cartItemRepository.deleteAll(items);
    }
}
