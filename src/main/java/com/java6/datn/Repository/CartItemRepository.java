package com.java6.datn.Repository;

import com.java6.datn.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByUserID(Integer userId);
    Optional<CartItem> findByUserIDAndProductIDAndProductSizeID(Integer userId, Integer productId, Integer productSizeId);
}

