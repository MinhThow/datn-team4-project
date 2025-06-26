package com.java6.datn.service;

import com.java6.datn.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewDTO> getAllReviews();
    List<ReviewDTO> getReviewsByProduct(Integer productID);
    List<ReviewDTO> getReviewsByUser(Integer userID);
    ReviewDTO getReviewById(Integer reviewID);
    ReviewDTO createReview(ReviewDTO reviewDTO);
    ReviewDTO updateReview(Integer reviewID, ReviewDTO reviewDTO);
    void deleteReview(Integer reviewID);
}

