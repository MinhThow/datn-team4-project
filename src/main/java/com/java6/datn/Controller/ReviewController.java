package com.java6.datn.Controller;

import com.java6.datn.DTO.ReviewDTO;
import com.java6.datn.Service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    
    @GetMapping("/product/{productID}")
    public List<ReviewDTO> getByProduct(@PathVariable Integer productID) {
        return reviewService.getReviewsByProductId(productID);
    }

  
    @PostMapping
    public ReviewDTO create(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.createReview(reviewDTO);
    }

    
    @GetMapping("/product/{productID}/average-rating")
    public Double getAverageRating(@PathVariable Integer productID) {
        return reviewService.getAverageRating(productID);
    }

    
    @GetMapping("/product/{productID}/total")
    public Integer getTotalReviews(@PathVariable Integer productID) {
        return reviewService.getTotalReviews(productID);
    }
    // ✅ NEW: Lấy đánh giá của user hiện tại cho sản phẩm (để hiển thị lại modal)
    @GetMapping("/user/{userID}/product/{productID}")
    public ReviewDTO getUserReviewForProduct(@PathVariable Integer userID, @PathVariable Integer productID) {
        List<ReviewDTO> reviews = reviewService.getReviewsByProductId(productID);
        return reviews.stream()
                .filter(r -> r.getUserId().equals(userID))
                .findFirst()
                .orElse(null);
    }
    
}

