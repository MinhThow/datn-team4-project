package com.java6.datn.Service;

import java.util.List;
import java.util.Optional;

import com.java6.datn.DTO.ReviewDTO;

public interface ReviewService {

	// === PRODUCT DETAIL OPERATIONS ===

	List<ReviewDTO> getReviewsByProductId(Integer productId);

	Double getAverageRating(Integer productId);

	Integer getTotalReviews(Integer productId);

	// === REVIEW MANAGEMENT OPERATIONS ===

	ReviewDTO createReview(ReviewDTO reviewDTO);

	// === UTILITY METHODS ===

	boolean hasUserReviewedProduct(Integer userId, Integer productId);

	List<ReviewDTO> getReviewsByProductIdWithPagination(Integer productId, int page, int size);
	Optional<Integer> getUserReviewRating(Integer userId, Integer productId);
	Optional<String> getUserReviewComment(Integer userId, Integer productId);

}
