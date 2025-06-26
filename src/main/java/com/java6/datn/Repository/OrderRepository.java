package com.java6.datn.repository;

import com.java6.datn.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByUserUserID(Integer userID); // để lấy orders của 1 user
}

