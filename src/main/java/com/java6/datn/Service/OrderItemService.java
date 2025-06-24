package com.java6.datn.Service;

import com.java6.datn.DTO.OrderItemDTO;

import java.util.List;

public interface OrderItemService {
    List<OrderItemDTO> getAllOrderItems();
    List<OrderItemDTO> getOrderItemsByOrder(Long orderID);
    OrderItemDTO getOrderItemById(Long id);
    OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO);
    OrderItemDTO updateOrderItem(Long id, OrderItemDTO orderItemDTO);
    void deleteOrderItem(Long id);
}

