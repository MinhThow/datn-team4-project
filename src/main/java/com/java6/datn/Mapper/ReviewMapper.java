package com.java6.datn.Mapper;

import com.java6.datn.DTO.ReviewDTO;
import com.java6.datn.Entity.Review;

import java.time.format.DateTimeFormatter;

public class ReviewMapper {
    public static ReviewDTO toDTO(Review entity) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(entity.getReviewID());
        dto.setProductId(entity.getProduct().getProductID());
        dto.setUserId(entity.getUser().getUserID());
        dto.setRating(entity.getRating());
        dto.setComment(entity.getComment());
        dto.setReviewDate(
                entity.getReviewDate() != null
                        ? entity.getReviewDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        : null
        );
        dto.setUserName(entity.getUser().getName()); // hoặc getUsername() tùy theo Entity
        dto.setProductName(entity.getProduct().getName());
        dto.setImageUrl(entity.getProduct().getImageUrl());
     // Trong ReviewMapper.java (cập nhật method toDTO)
        dto.setOrderId(entity.getOrder() != null ? entity.getOrder().getOrderId() : null);

        return dto;
    }
}

