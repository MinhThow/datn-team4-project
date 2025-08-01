package com.java6.datn.Repository;


import com.java6.datn.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserUserID(Integer userID);

    // Đếm số đơn hàng đã thanh toán
    long countByStatus(String status);

    // Lấy tất cả đơn hàng đã thanh toán
    List<Order> findByStatus(String status);
}

