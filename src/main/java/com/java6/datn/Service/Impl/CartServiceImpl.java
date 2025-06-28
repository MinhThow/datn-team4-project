package com.java6.datn.Service.Impl;

import com.java6.datn.DTO.CartItemDTO;
import com.java6.datn.DTO.ProductDTO;
import com.java6.datn.Service.CartItemService;
import com.java6.datn.Service.CartService;
import com.java6.datn.Service.ProductService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemService cartItemService;
    private final ProductService productService;

    public CartServiceImpl(CartItemService cartItemService, ProductService productService) {
        this.cartItemService = cartItemService;
        this.productService = productService;
    }

    @Override
    public int getCartItemCount(Integer userID) {
        if (userID == null) {
            return 0;
        }
        try {
            List<CartItemDTO> cartItems = cartItemService.getCartItemsByUser(userID);
            return cartItems.stream()
                    .mapToInt(CartItemDTO::getQuantity)
                    .sum();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public BigDecimal getCartTotal(Integer userID) {
        if (userID == null) {
            return BigDecimal.ZERO;
        }
        try {
            List<CartItemDTO> cartItems = cartItemService.getCartItemsByUser(userID);
            return cartItems.stream()
                    .map(item -> {
                        try {
                            ProductDTO product = productService.getProductById(item.getProductID());
                            return product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                        } catch (Exception e) {
                            return BigDecimal.ZERO;
                        }
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
} 