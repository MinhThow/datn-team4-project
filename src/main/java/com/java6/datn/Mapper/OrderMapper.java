package com.java6.datn.Mapper;

import com.java6.datn.DTO.OrderDTO;
import com.java6.datn.Entity.Order;

public class OrderMapper {

    public static OrderDTO toDTO(Order entity) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderID(entity.getOrderID());
        dto.setUserID(entity.getUser().getUserID());
        dto.setTotal(entity.getTotal());
        dto.setStatus(entity.getStatus());
        dto.setOrderDate(entity.getOrderDate());
        dto.setShippingAddress(entity.getShippingAddress());
        dto.setPaymentMethodID(entity.getPaymentMethod() != null ? entity.getPaymentMethod().getPaymentMethodID() : null);
        return dto;
    }
}

