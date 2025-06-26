package com.java6.datn.repository;

import com.java6.datn.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    List<CartItem> findByUserUserID(Integer userID);
}

