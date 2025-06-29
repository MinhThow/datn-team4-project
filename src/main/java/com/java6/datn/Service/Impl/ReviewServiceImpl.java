package com.java6.datn.Service.Impl;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java6.datn.DTO.ReviewDTO;
import com.java6.datn.Entity.Product;
import com.java6.datn.Entity.Review;
import com.java6.datn.Entity.User;
import com.java6.datn.Mapper.ReviewMapper;
import com.java6.datn.Repository.ProductRepository;
import com.java6.datn.Repository.ReviewRepository;
import com.java6.datn.Repository.UserRepository;
import com.java6.datn.Service.ReviewService;

/**
 * ReviewServiceImpl - Implementation của ReviewService interface
 * 
 * <p>Service này implement business logic cho review operations:</p>
 * <ul>
 *   <li>Product detail support: reviews, ratings, statistics</li>
 *   <li>Review management: create, validate reviews</li>
 *   <li>Performance optimization: caching, pagination</li>
 * </ul>
 * 
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li>Comprehensive error handling và validation</li>
 *   <li>Performance optimization với database queries</li>
 *   <li>Business rules enforcement</li>
 *   <li>Logging cho debugging và monitoring</li>
 * </ul>
 * 
 * @author DATN Team 4
 * @version 1.0
 * @since 2025-01-16
 */
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int MAX_REVIEWS_DISPLAY = 50; // Limit reviews để tránh performance issues
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // === PRODUCT DETAIL OPERATIONS ===
    
    /**
     * {@inheritDoc}
     * 
     * <p><strong>Implementation Details:</strong></p>
     * <ul>
     *   <li>Validate productId parameter</li>
     *   <li>Query reviews với sorting by date DESC</li>
     *   <li>Limit to MAX_REVIEWS_DISPLAY để performance</li>
     *   <li>Map entities to DTOs với user information</li>
     *   <li>Format review dates cho display</li>
     * </ul>
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByProductId(Integer productId) {
        logger.info("Getting reviews for product ID: {}", productId);
        
        // Step 1: Validate input parameters
        if (productId == null || productId <= 0) {
            logger.error("Invalid product ID: {}", productId);
            throw new IllegalArgumentException("Product ID must be positive integer");
        }
        
        try {
            // Step 2: Query reviews từ database
            List<Review> reviews = reviewRepository.findByProductProductIDOrderByReviewDateDesc(productId);
            
            // Step 3: Limit results để avoid performance issues
            if (reviews.size() > MAX_REVIEWS_DISPLAY) {
                logger.warn("Product {} has {} reviews, limiting to {}", productId, reviews.size(), MAX_REVIEWS_DISPLAY);
                reviews = reviews.subList(0, MAX_REVIEWS_DISPLAY);
            }
            
            // Step 4: Convert entities to DTOs với user information
            List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
            logger.info("Retrieved {} reviews for product {}", reviewDTOs.size(), productId);
            return reviewDTOs;
            
        } catch (Exception e) {
            logger.error("Error retrieving reviews for product {}: {}", productId, e.getMessage());
            throw new RuntimeException("Failed to retrieve reviews", e);
        }
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p><strong>Implementation Details:</strong></p>
     * <ul>
     *   <li>Use custom query cho performance</li>
     *   <li>Handle null case (no reviews)</li>
     *   <li>Round to 1 decimal place</li>
     *   <li>Cache result for future calls (TODO)</li>
     * </ul>
     */
    @Override
    @Transactional(readOnly = true)
    public Double getAverageRating(Integer productId) {
        logger.debug("Calculating average rating for product ID: {}", productId);
        
        // Step 1: Validate input
        if (productId == null || productId <= 0) {
            logger.error("Invalid product ID: {}", productId);
            throw new IllegalArgumentException("Product ID must be positive integer");
        }
        
        try {
            // Step 2: Query average rating từ database
            Double avgRating = reviewRepository.findAverageRatingByProductId(productId);
            
            // Step 3: Handle null case (no reviews)
            if (avgRating == null) {
                logger.debug("No reviews found for product {}, returning 0.0", productId);
                return 0.0;
            }
            
            // Step 4: Round to 1 decimal place
            double roundedRating = Math.round(avgRating * 10.0) / 10.0;
            
            logger.debug("Average rating for product {}: {}", productId, roundedRating);
            return roundedRating;
            
        } catch (Exception e) {
            logger.error("Error calculating average rating for product {}: {}", productId, e.getMessage());
            throw new RuntimeException("Failed to calculate average rating", e);
        }
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p><strong>Implementation Details:</strong></p>
     * <ul>
     *   <li>Use count query cho performance</li>
     *   <li>Convert Long to Integer</li>
     *   <li>Handle edge cases</li>
     * </ul>
     */
    @Override
    @Transactional(readOnly = true)
    public Integer getTotalReviews(Integer productId) {
        logger.debug("Counting total reviews for product ID: {}", productId);
        
        // Step 1: Validate input
        if (productId == null || productId <= 0) {
            logger.error("Invalid product ID: {}", productId);
            throw new IllegalArgumentException("Product ID must be positive integer");
        }
        
        try {
            // Step 2: Count reviews
            Long count = reviewRepository.countByProductProductID(productId);
            
            // Step 3: Convert to Integer và handle null
            Integer totalReviews = (count != null) ? count.intValue() : 0;
            
            logger.debug("Total reviews for product {}: {}", productId, totalReviews);
            return totalReviews;
            
        } catch (Exception e) {
            logger.error("Error counting reviews for product {}: {}", productId, e.getMessage());
            throw new RuntimeException("Failed to count reviews", e);
        }
    }
    
    // === REVIEW MANAGEMENT OPERATIONS ===
    
    /**
     * {@inheritDoc}
     * 
     * <p><strong>Implementation Details:</strong></p>
     * <ul>
     *   <li>Comprehensive validation của input data</li>
     *   <li>Check duplicate reviews</li>
     *   <li>Verify product và user existence</li>
     *   <li>Set review date và save</li>
     * </ul>
     */
    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        logger.info("Creating new review for product {} by user {}", 
                   reviewDTO.getProductID(), reviewDTO.getUserID());
        
        // Step 1: Validate input data
        validateReviewData(reviewDTO);
        
        // Step 2: Check if user already reviewed this product
        if (hasUserReviewedProduct(reviewDTO.getUserID(), reviewDTO.getProductID())) {
            logger.error("User {} already reviewed product {}", 
                        reviewDTO.getUserID(), reviewDTO.getProductID());
            throw new RuntimeException("User has already reviewed this product");
        }
        
        // Step 3: Verify product exists
        Product product = productRepository.findById(reviewDTO.getProductID())
            .orElseThrow(() -> {
                logger.error("Product not found: {}", reviewDTO.getProductID());
                return new RuntimeException("Product not found");
            });
            
        // Step 4: Verify user exists
        User user = userRepository.findById(reviewDTO.getUserID())
            .orElseThrow(() -> {
                logger.error("User not found: {}", reviewDTO.getUserID());
                return new RuntimeException("User not found");
            });
        
        try {
            // Step 5: Create new Review entity
            Review review = new Review();
            review.setProduct(product);
            review.setUser(user);
            review.setRating(reviewDTO.getRating());
            review.setComment(reviewDTO.getComment());
            // reviewDate sẽ được set automatically trong entity
            
            Review savedReview = reviewRepository.save(review);
            
            // Step 6: Convert back to DTO
            ReviewDTO savedDTO = convertToDTO(savedReview);
            
            logger.info("Successfully created review with ID: {}", savedReview.getReviewID());
            return savedDTO;
            
        } catch (Exception e) {
            logger.error("Error creating review: {}", e.getMessage());
            throw new RuntimeException("Failed to create review", e);
        }
    }
    
    // === UTILITY METHODS ===
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasUserReviewedProduct(Integer userId, Integer productId) {
        if (userId == null || productId == null) {
            return false;
        }
        
        List<Review> existingReviews = reviewRepository.findByProductProductID(productId);
        return existingReviews.stream()
            .anyMatch(review -> review.getUser().getUserID().equals(userId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByProductIdWithPagination(Integer productId, int page, int size) {
        logger.debug("Getting reviews for product {} with pagination: page={}, size={}", 
                    productId, page, size);
        
        // Validate input
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive integer");
        }
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters");
        }
        
        try {
            // Get all reviews và implement manual pagination
            List<Review> allReviews = reviewRepository.findByProductProductIDOrderByReviewDateDesc(productId);
            
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, allReviews.size());
            
            if (startIndex >= allReviews.size()) {
                return List.of(); // Empty list for pages beyond data
            }
            
            List<Review> pageReviews = allReviews.subList(startIndex, endIndex);
            
            return pageReviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            logger.error("Error getting paginated reviews: {}", e.getMessage());
            throw new RuntimeException("Failed to get paginated reviews", e);
        }
    }
    
    // === PRIVATE HELPER METHODS ===
    
    /**
     * Convert Review entity to ReviewDTO với user information
     */
    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = ReviewMapper.toDTO(review);
        
        // Add user name cho display
        if (review.getUser() != null) {
            dto.setUserName(review.getUser().getName());
        }
        
        // Format review date với custom format
        if (review.getReviewDate() != null) {
            dto.setReviewDate(review.getReviewDate().format(DATE_FORMATTER));
        }
        
        return dto;
    }
    
    /**
     * Validate review data trước khi create
     */
    private void validateReviewData(ReviewDTO reviewDTO) {
        if (reviewDTO == null) {
            throw new IllegalArgumentException("Review data cannot be null");
        }
        
        if (reviewDTO.getProductID() == null || reviewDTO.getProductID() <= 0) {
            throw new IllegalArgumentException("Valid product ID is required");
        }
        
        if (reviewDTO.getUserID() == null || reviewDTO.getUserID() <= 0) {
            throw new IllegalArgumentException("Valid user ID is required");
        }
        
        if (reviewDTO.getRating() == null || reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
        if (reviewDTO.getComment() == null || reviewDTO.getComment().trim().isEmpty()) {
            throw new IllegalArgumentException("Review comment cannot be empty");
        }
        
        if (reviewDTO.getComment().length() > 500) {
            throw new IllegalArgumentException("Review comment cannot exceed 500 characters");
        }
    }
}
