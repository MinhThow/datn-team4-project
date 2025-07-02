package com.java6.datn.Mapper;

import com.java6.datn.DTO.OrderDTO;
import com.java6.datn.DTO.OrderItemDTO;
import com.java6.datn.Entity.Order;

import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDTO toDTO(Order entity) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderID(entity.getOrderID());
        dto.setUserID(entity.getUser().getUserID());
        dto.setName(entity.getUser().getName());
        dto.setTotal(entity.getTotal());
        dto.setStatus(entity.getStatus());
        dto.setOrderDate(entity.getOrderDate());
        dto.setShippingAddress(entity.getShippingAddress());
        dto.setPaymentMethodID(entity.getPaymentMethod() != null ? entity.getPaymentMethod().getPaymentMethodID() : null);
        dto.setPaymentMethodName(entity.getPaymentMethod() != null ? entity.getPaymentMethod().getName() : null);
        if (entity.getOrderItems() != null) {
            dto.setOrderItemsDTO(entity.getOrderItems().stream().map(item -> {
                OrderItemDTO itemDTO = new OrderItemDTO();
                itemDTO.setOrderItemID(item.getOrderItemID());
                itemDTO.setOrderID(entity.getOrderID());
                itemDTO.setProductID(item.getProduct().getProductID());
                itemDTO.setProductName(item.getProduct().getName());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPrice(item.getPrice());
                return itemDTO;
            }).collect(Collectors.toList()));
        }
        return dto;
    }
}

