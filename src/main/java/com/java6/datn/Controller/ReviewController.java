package com.java6.datn.Controller;

import com.java6.datn.DTO.ReviewDTO;
import com.java6.datn.Service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ReviewController - REST API Controller cho quản lý reviews
 * 
 * <p>Controller này cung cấp các REST API endpoints để:</p>
 * <ul>
 *   <li>Lấy reviews theo product ID</li>
 *   <li>Tạo review mới</li>
 *   <li>Lấy thống kê rating</li>
 * </ul>
 * 
 * <p><strong>Note:</strong> Controller này được redesign để support product detail functionality.
 * Một số endpoints cũ đã được move sang ProductController cho better organization.</p>
 * 
 * @author DATN Team 4
 * @version 2.0
 * @since 2025-01-16
 * @see com.java6.datn.Controller.ProductController ProductController cho product detail endpoints
 */
@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Lấy reviews theo product ID
     * 
     * <p><strong>Note:</strong> Endpoint này có thể được deprecated trong tương lai.
     * Recommend sử dụng ProductController.getProductDetail() hoặc ProductController.getProductReviews()
     * để có better integration với product detail functionality.</p>
     * 
     * @param productID ID của sản phẩm
     * @return List&lt;ReviewDTO&gt; danh sách reviews của sản phẩm
     */
    @GetMapping("/product/{productID}")
    public List<ReviewDTO> getByProduct(@PathVariable Integer productID) {
        return reviewService.getReviewsByProductId(productID);
    }

    /**
     * Tạo review mới
     * 
     * <p><strong>Note:</strong> Recommend sử dụng ProductController.addProductReview()
     * để có better validation và integration với product detail page.</p>
     * 
     * @param reviewDTO thông tin review mới
     * @return ReviewDTO review đã được tạo
     */
    @PostMapping
    public ReviewDTO create(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.createReview(reviewDTO);
    }

    /**
     * Lấy average rating của sản phẩm
     * 
     * @param productID ID của sản phẩm
     * @return Double average rating (0.0 nếu chưa có review)
     */
    @GetMapping("/product/{productID}/average-rating")
    public Double getAverageRating(@PathVariable Integer productID) {
        return reviewService.getAverageRating(productID);
    }

    /**
     * Lấy total reviews của sản phẩm
     * 
     * @param productID ID của sản phẩm
     * @return Integer total number of reviews
     */
    @GetMapping("/product/{productID}/total")
    public Integer getTotalReviews(@PathVariable Integer productID) {
        return reviewService.getTotalReviews(productID);
    }

    // === TEMPORARILY DISABLED ENDPOINTS ===
    // These endpoints are not supported in the new ReviewService interface
    // They may be re-implemented in future versions if needed

    /*
    @GetMapping
    public List<ReviewDTO> getAll() {
        // TODO: Implement if needed - not in current ReviewService interface
        throw new UnsupportedOperationException("This endpoint is temporarily disabled. Use ProductController endpoints instead.");
    }

    @GetMapping("/user/{userID}")
    public List<ReviewDTO> getByUser(@PathVariable Integer userID) {
        // TODO: Implement if needed - not in current ReviewService interface
        throw new UnsupportedOperationException("This endpoint is temporarily disabled.");
    }

    @GetMapping("/{reviewID}")
    public ReviewDTO getById(@PathVariable Integer reviewID) {
        // TODO: Implement if needed - not in current ReviewService interface
        throw new UnsupportedOperationException("This endpoint is temporarily disabled.");
    }

    @PutMapping("/{reviewID}")
    public ReviewDTO update(@PathVariable Integer reviewID, @RequestBody ReviewDTO reviewDTO) {
        // TODO: Implement if needed - not in current ReviewService interface
        throw new UnsupportedOperationException("This endpoint is temporarily disabled.");
    }

    @DeleteMapping("/{reviewID}")
    public void delete(@PathVariable Integer reviewID) {
        // TODO: Implement if needed - not in current ReviewService interface
        throw new UnsupportedOperationException("This endpoint is temporarily disabled.");
    }
    */
}

