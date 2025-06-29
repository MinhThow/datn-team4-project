package com.java6.datn.Service;

import java.util.List;

import com.java6.datn.DTO.ReviewDTO;

/**
 * ReviewService - Interface định nghĩa các service methods cho quản lý reviews
 * 
 * <p>Interface này định nghĩa tất cả các operations liên quan đến reviews trong hệ thống e-commerce:</p>
 * 
 * <ul>
 *   <li><strong>Product Detail Support:</strong> Lấy reviews, rating statistics cho product pages</li>
 *   <li><strong>Review Management:</strong> Create, read reviews</li>
 *   <li><strong>Statistical Operations:</strong> Average rating, review count calculations</li>
 * </ul>
 * 
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Reviews chỉ có thể tạo bởi authenticated users</li>
 *   <li>Mỗi user chỉ có thể review 1 lần cho 1 product</li>
 *   <li>Rating phải từ 1-5 stars</li>
 *   <li>Comment không được empty (validation)</li>
 * </ul>
 * 
 * <p><strong>Implementation:</strong> ReviewServiceImpl</p>
 * <p><strong>Repository Layer:</strong> ReviewRepository</p>
 * <p><strong>Entity:</strong> Review</p>
 * <p><strong>DTO:</strong> ReviewDTO</p>
 * 
 * @author DATN Team 4
 * @version 1.0
 * @since 2025-01-16
 * @see com.java6.datn.Service.Impl.ReviewServiceImpl
 * @see com.java6.datn.Repository.ReviewRepository
 * @see com.java6.datn.Entity.Review
 * @see com.java6.datn.DTO.ReviewDTO
 */
public interface ReviewService {
    
    // === PRODUCT DETAIL OPERATIONS ===
    
    /**
     * Lấy danh sách reviews cho một sản phẩm
     * 
     * <p>Trả về tất cả reviews cho sản phẩm được sắp xếp theo ngày mới nhất.
     * Sử dụng cho product detail page để hiển thị customer feedback.</p>
     * 
     * <p><strong>Business Logic:</strong></p>
     * <ul>
     *   <li>Sắp xếp theo reviewDate DESC (mới nhất trước)</li>
     *   <li>Include user information (name) trong ReviewDTO</li>
     *   <li>Format reviewDate cho display</li>
     *   <li>Handle case khi product không có reviews</li>
     * </ul>
     * 
     * <p><strong>Performance Considerations:</strong></p>
     * <ul>
     *   <li>Limit to 50 reviews để tránh performance issues</li>
     *   <li>Consider pagination cho products có nhiều reviews</li>
     *   <li>Lazy loading user information</li>
     * </ul>
     * 
     * @param productId ID của sản phẩm
     * @return List&lt;ReviewDTO&gt; danh sách reviews, empty list nếu không có reviews
     * @throws IllegalArgumentException nếu productId null hoặc <= 0
     * 
     * @apiExample
     * <pre>
     * // Usage example:
     * List&lt;ReviewDTO&gt; reviews = reviewService.getReviewsByProductId(5);
     * // Returns: [
     * //   {reviewID: 1, userName: "Nguyễn Minh Thơ", rating: 5, comment: "Túi xách da thật...", reviewDate: "2025-01-15"},
     * //   {reviewID: 2, userName: "Lê Thị Hoa", rating: 4, comment: "Chất lượng tốt...", reviewDate: "2025-01-14"}
     * // ]
     * </pre>
     */
    List<ReviewDTO> getReviewsByProductId(Integer productId);
    
    /**
     * Tính average rating cho một sản phẩm
     * 
     * <p>Tính toán rating trung bình từ tất cả reviews của sản phẩm.
     * Sử dụng để hiển thị star rating trên product detail page.</p>
     * 
     * <p><strong>Calculation Logic:</strong></p>
     * <ul>
     *   <li>Chỉ tính reviews có rating > 0</li>
     *   <li>Round đến 1 decimal place</li>
     *   <li>Trả về 0.0 nếu không có reviews</li>
     *   <li>Cache kết quả để improve performance</li>
     * </ul>
     * 
     * <p><strong>Use Cases:</strong></p>
     * <ul>
     *   <li>Product detail page star display</li>
     *   <li>Product listing page ratings</li>
     *   <li>Search result sorting by rating</li>
     * </ul>
     * 
     * @param productId ID của sản phẩm
     * @return Double average rating (0.0 - 5.0), 0.0 nếu không có reviews
     * @throws IllegalArgumentException nếu productId null hoặc <= 0
     * 
     * @apiExample
     * <pre>
     * // Usage example:
     * Double avgRating = reviewService.getAverageRating(5);
     * // Returns: 4.5 (calculated from reviews: 5, 4, 5, 4)
     * </pre>
     */
    Double getAverageRating(Integer productId);
    
    /**
     * Đếm tổng số reviews cho một sản phẩm
     * 
     * <p>Trả về tổng số reviews để hiển thị "X Reviews" trên UI.
     * Sử dụng cho product detail page và product cards.</p>
     * 
     * <p><strong>Business Logic:</strong></p>
     * <ul>
     *   <li>Đếm tất cả reviews (kể cả rating thấp)</li>
     *   <li>Không đếm reviews bị soft-deleted (nếu có)</li>
     *   <li>Cache count để improve performance</li>
     * </ul>
     * 
     * @param productId ID của sản phẩm
     * @return Integer số lượng reviews, 0 nếu không có reviews
     * @throws IllegalArgumentException nếu productId null hoặc <= 0
     * 
     * @apiExample
     * <pre>
     * // Usage example:
     * Integer totalReviews = reviewService.getTotalReviews(5);
     * // Returns: 8 (total number of reviews for product ID 5)
     * </pre>
     */
    Integer getTotalReviews(Integer productId);
    
    // === REVIEW MANAGEMENT OPERATIONS ===
    
    /**
     * Tạo review mới cho sản phẩm
     * 
     * <p>Tạo review mới từ user cho sản phẩm. Thực hiện validation
     * và business rules checking trước khi save.</p>
     * 
     * <p><strong>Validation Rules:</strong></p>
     * <ul>
     *   <li>User phải authenticated</li>
     *   <li>Rating từ 1-5</li>
     *   <li>Comment không empty và <= 500 characters</li>
     *   <li>User chưa review product này trước đó</li>
     *   <li>Product phải tồn tại và active</li>
     * </ul>
     * 
     * <p><strong>Business Logic:</strong></p>
     * <ul>
     *   <li>Set reviewDate = current timestamp</li>
     *   <li>Associate với authenticated user</li>
     *   <li>Update product average rating cache</li>
     *   <li>Send notification đến product owner (future)</li>
     * </ul>
     * 
     * @param reviewDTO Review data từ form submission
     * @return ReviewDTO review đã được tạo (bao gồm ID và reviewDate)
     * @throws IllegalArgumentException nếu validation fails
     * @throws RuntimeException nếu user đã review product này
     * @throws RuntimeException nếu product không tồn tại
     * 
     * @apiExample
     * <pre>
     * // Usage example:
     * ReviewDTO newReview = new ReviewDTO();
     * newReview.setProductID(5);
     * newReview.setUserID(2);
     * newReview.setRating(5);
     * newReview.setComment("Sản phẩm tuyệt vời!");
     * 
     * ReviewDTO savedReview = reviewService.createReview(newReview);
     * // Returns: ReviewDTO with ID=9, reviewDate=2025-01-16T10:30:00
     * </pre>
     */
    ReviewDTO createReview(ReviewDTO reviewDTO);
    
    // === UTILITY METHODS ===
    
    /**
     * Kiểm tra user đã review product chưa
     * 
     * <p>Utility method để check duplicate reviews trước khi create.</p>
     * 
     * @param userId ID của user
     * @param productId ID của sản phẩm
     * @return boolean true nếu user đã review, false nếu chưa
     */
    boolean hasUserReviewedProduct(Integer userId, Integer productId);
    
    /**
     * Lấy reviews với pagination
     * 
     * <p>Cho products có nhiều reviews, support pagination để improve performance.</p>
     * 
     * @param productId ID của sản phẩm
     * @param page Page number (0-based)
     * @param size Number of reviews per page
     * @return List&lt;ReviewDTO&gt; reviews cho page được request
     */
    List<ReviewDTO> getReviewsByProductIdWithPagination(Integer productId, int page, int size);
}

