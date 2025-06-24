package com.java6.datn.Service;

import com.java6.datn.DTO.ReviewDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewDTO> getAllReviews();
    List<ReviewDTO> getReviewsByProduct(Long productID);
    List<ReviewDTO> getReviewsByUser(Long userID);
    ReviewDTO getReviewById(Long reviewID);
    ReviewDTO createReview(ReviewDTO reviewDTO);
    ReviewDTO updateReview(Long reviewID, ReviewDTO reviewDTO);
    void deleteReview(Long reviewID);
}

