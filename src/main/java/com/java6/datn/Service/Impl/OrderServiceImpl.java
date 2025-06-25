package com.java6.datn.Service.Impl;

import com.java6.datn.DTO.OrderDTO;
import com.java6.datn.Entity.Order;
import com.java6.datn.Entity.PaymentMethod;
import com.java6.datn.Entity.User;
import com.java6.datn.Mapper.OrderMapper;
import com.java6.datn.Repository.OrderRepository;
import com.java6.datn.Repository.PaymentMethodRepository;
import com.java6.datn.Repository.UserRepository;
import com.java6.datn.Service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            PaymentMethodRepository paymentMethodRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(OrderMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByUser(Integer userID) {
        return orderRepository.findByUserUserID(userID).stream().map(OrderMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(Integer id) {
        return OrderMapper.toDTO(orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found")));
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserID()).orElseThrow(() -> new RuntimeException("User not found"));
        PaymentMethod paymentMethod = null;
        if (orderDTO.getPaymentMethodID() != null) {
            paymentMethod = paymentMethodRepository.findById(orderDTO.getPaymentMethodID()).orElseThrow(() -> new RuntimeException("Payment method not found"));
        }
        Order order = new Order();
        order.setUser(user);
        order.setTotal(orderDTO.getTotal());
        order.setStatus(orderDTO.getStatus());
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setPaymentMethod(paymentMethod);
        return OrderMapper.toDTO(orderRepository.save(order));
    }

    @Override
    public OrderDTO updateOrder(Integer id, OrderDTO orderDTO) {
        Order existing = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        if (orderDTO.getUserID() != null) {
            existing.setUser(userRepository.findById(orderDTO.getUserID()).orElseThrow(() -> new RuntimeException("User not found")));
        }
        existing.setTotal(orderDTO.getTotal());
        existing.setStatus(orderDTO.getStatus());
        existing.setShippingAddress(orderDTO.getShippingAddress());
        if (orderDTO.getPaymentMethodID() != null) {
            existing.setPaymentMethod(
                    paymentMethodRepository.findById(orderDTO.getPaymentMethodID()).orElseThrow(() -> new RuntimeException("Payment method not found"))
            );
        }
        return OrderMapper.toDTO(orderRepository.save(existing));
    }

    @Override
    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }
}

