package com.java6.datn.Mapper;

import com.java6.datn.DTO.OrderItemDTO;
import com.java6.datn.Entity.OrderItem;

public class OrderItemMapper {

    public static OrderItemDTO toDTO(OrderItem entity) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setOrderItemID(entity.getOrderItemID());
        dto.setOrderID(entity.getOrder().getOrderID());
        dto.setProductID(entity.getProduct().getProductID());
        dto.setQuantity(entity.getQuantity());
        dto.setPrice(entity.getPrice());
        return dto;
    }
}

