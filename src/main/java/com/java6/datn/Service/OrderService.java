package com.java6.datn.Service;

import com.java6.datn.DTO.OrderDTO;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAllOrders();
    List<OrderDTO> getOrdersByUser(Integer userID);
    OrderDTO getOrderById(Integer id);
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderDTO updateOrder(Integer id, OrderDTO orderDTO);
    void deleteOrder(Integer id);
}
