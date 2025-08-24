package com.java6.datn.Service;

import com.java6.datn.DTO.OrderRequestDTO;
import com.java6.datn.DTO.OrderResponseDTO;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);
    List<OrderResponseDTO> getAllOrders();
    OrderResponseDTO getOrderById(Integer orderId);
    OrderResponseDTO updateOrderStatus(Integer orderId, String status);
}
