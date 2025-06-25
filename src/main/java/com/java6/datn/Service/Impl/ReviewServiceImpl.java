package com.java6.datn.Service.Impl;

import com.java6.datn.DTO.ReviewDTO;
import com.java6.datn.Entity.Product;
import com.java6.datn.Entity.Review;
import com.java6.datn.Entity.User;
import com.java6.datn.Mapper.ReviewMapper;
import com.java6.datn.Repository.ProductRepository;
import com.java6.datn.Repository.ReviewRepository;
import com.java6.datn.Repository.UserRepository;
import com.java6.datn.Service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             ProductRepository productRepository,
                             UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream().map(ReviewMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getReviewsByProduct(Integer productID) {
        return reviewRepository.findByProductProductID(productID).stream().map(ReviewMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getReviewsByUser(Integer userID) {
        return reviewRepository.findByUserUserID(userID).stream().map(ReviewMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public ReviewDTO getReviewById(Integer reviewID) {
        return ReviewMapper.toDTO(
                reviewRepository.findById(reviewID).orElseThrow(() -> new RuntimeException("Review not found"))
        );
    }

    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Product product = productRepository.findById(reviewDTO.getProductID())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        User user = userRepository.findById(reviewDTO.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        return ReviewMapper.toDTO(reviewRepository.save(review));
    }

    @Override
    public ReviewDTO updateReview(Integer reviewID, ReviewDTO reviewDTO) {
        Review existing = reviewRepository.findById(reviewID)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        existing.setRating(reviewDTO.getRating());
        existing.setComment(reviewDTO.getComment());
        return ReviewMapper.toDTO(reviewRepository.save(existing));
    }

    @Override
    public void deleteReview(Integer reviewID) {
        reviewRepository.deleteById(reviewID);
    }
}
