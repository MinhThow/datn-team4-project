package com.java6.datn.mapper;

import com.java6.datn.dto.ReviewDTO;
import com.java6.datn.entity.Review;

import java.time.format.DateTimeFormatter;

public class ReviewMapper {
    public static ReviewDTO toDTO(Review entity) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewID(entity.getReviewID());
        dto.setProductID(entity.getProduct().getProductID());
        dto.setUserID(entity.getUser().getUserID());
        dto.setRating(entity.getRating());
        dto.setComment(entity.getComment());
        dto.setReviewDate(
                entity.getReviewDate() != null
                        ? entity.getReviewDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        : null
        );
        return dto;
    }
}

