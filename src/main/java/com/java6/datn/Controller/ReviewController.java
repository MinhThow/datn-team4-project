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

    @GetMapping
    public List<ReviewDTO> getAll() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/product/{productID}")
    public List<ReviewDTO> getByProduct(@PathVariable Long productID) {
        return reviewService.getReviewsByProduct(productID);
    }

    @GetMapping("/user/{userID}")
    public List<ReviewDTO> getByUser(@PathVariable Long userID) {
        return reviewService.getReviewsByUser(userID);
    }

    @GetMapping("/{reviewID}")
    public ReviewDTO getById(@PathVariable Long reviewID) {
        return reviewService.getReviewById(reviewID);
    }

    @PostMapping
    public ReviewDTO create(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.createReview(reviewDTO);
    }

    @PutMapping("/{reviewID}")
    public ReviewDTO update(@PathVariable Long reviewID, @RequestBody ReviewDTO reviewDTO) {
        return reviewService.updateReview(reviewID, reviewDTO);
    }

    @DeleteMapping("/{reviewID}")
    public void delete(@PathVariable Long reviewID) {
        reviewService.deleteReview(reviewID);
    }
}

