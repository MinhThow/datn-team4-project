package com.java6.datn.Service.Impl;

import com.java6.datn.DTO.OrderItemDTO;
import com.java6.datn.Entity.Order;
import com.java6.datn.Entity.OrderItem;
import com.java6.datn.Entity.Product;
import com.java6.datn.Mapper.OrderItemMapper;
import com.java6.datn.Repository.OrderItemRepository;
import com.java6.datn.Repository.OrderRepository;
import com.java6.datn.Repository.ProductRepository;
import com.java6.datn.Service.OrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository,
                                OrderRepository orderRepository,
                                ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<OrderItemDTO> getAllOrderItems() {
        return orderItemRepository.findAll().stream()
                .map(OrderItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderItemDTO> getOrderItemsByOrder(Long orderID) {
        return orderItemRepository.findByOrderOrderID(orderID).stream()
                .map(OrderItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemDTO getOrderItemById(Long id) {
        return OrderItemMapper.toDTO(orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found")));
    }

    @Override
    public OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO) {
        Order order = orderRepository.findById(orderItemDTO.getOrderID())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Product product = productRepository.findById(orderItemDTO.getProductID())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        OrderItem entity = new OrderItem();
        entity.setOrder(order);
        entity.setProduct(product);
        entity.setQuantity(orderItemDTO.getQuantity());
        entity.setPrice(orderItemDTO.getPrice());

        return OrderItemMapper.toDTO(orderItemRepository.save(entity));
    }

    @Override
    public OrderItemDTO updateOrderItem(Long id, OrderItemDTO orderItemDTO) {
        OrderItem existing = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));

        if (orderItemDTO.getOrderID() != null) {
            existing.setOrder(orderRepository.findById(orderItemDTO.getOrderID())
                    .orElseThrow(() -> new RuntimeException("Order not found")));
        }
        if (orderItemDTO.getProductID() != null) {
            existing.setProduct(productRepository.findById(orderItemDTO.getProductID())
                    .orElseThrow(() -> new RuntimeException("Product not found")));
        }
        existing.setQuantity(orderItemDTO.getQuantity());
        existing.setPrice(orderItemDTO.getPrice());

        return OrderItemMapper.toDTO(orderItemRepository.save(existing));
    }

    @Override
    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);
    }
}
