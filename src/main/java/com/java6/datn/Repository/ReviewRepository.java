package com.java6.datn.Repository;

import com.java6.datn.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductProductID(Long productID); // lấy danh sách review theo sản phẩm
    List<Review> findByUserUserID(Long userID);           // lấy danh sách review của user
}

