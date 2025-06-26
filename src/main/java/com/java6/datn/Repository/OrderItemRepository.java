package com.java6.datn.repository;

import com.java6.datn.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderOrderID(Integer orderID); // lấy orderItems của 1 order
}

