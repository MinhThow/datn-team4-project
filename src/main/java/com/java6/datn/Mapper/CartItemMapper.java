package com.java6.datn.Mapper;

import com.java6.datn.DTO.CartItemDTO;
import com.java6.datn.Entity.CartItem;

public class CartItemMapper {

    public static CartItemDTO toDTO(CartItem entity) {
        CartItemDTO dto = new CartItemDTO();
        dto.setCartItemID(entity.getCartItemID());
        dto.setUserID(entity.getUser().getUserID());
        dto.setProductID(entity.getProduct().getProductID());
        dto.setQuantity(entity.getQuantity());
        dto.setAddedAt(entity.getAddedAt());
        return dto;
    }

    public static void updateEntityFromDTO(CartItem entity, CartItemDTO dto) {
        entity.setQuantity(dto.getQuantity());
    }
}

