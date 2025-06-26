package com.java6.datn.service;

import com.java6.datn.dto.OrderItemDTO;

import java.util.List;

public interface OrderItemService {
    List<OrderItemDTO> getAllOrderItems();
    List<OrderItemDTO> getOrderItemsByOrder(Integer orderID);
    OrderItemDTO getOrderItemById(Integer id);
    OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO);
    OrderItemDTO updateOrderItem(Integer id, OrderItemDTO orderItemDTO);
    void deleteOrderItem(Integer id);
}

