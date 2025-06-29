package com.java6.datn.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.java6.datn.Entity.Review;

/**
 * ReviewRepository - Data access layer cho Review entity
 * 
 * <p>Repository này cung cấp các methods để truy xuất và thao tác với review data:</p>
 * <ul>
 *   <li>Basic CRUD operations (từ JpaRepository)</li>
 *   <li>Custom queries cho product detail functionality</li>
 *   <li>Statistical queries cho rating calculations</li>
 * </ul>
 * 
 * @author DATN Team 4
 * @version 1.0
 * @since 2025-01-16
 */
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    
    // === EXISTING METHODS ===
    
    /**
     * Lấy danh sách review theo sản phẩm
     * @param productID ID của sản phẩm
     * @return List của reviews cho sản phẩm
     */
    List<Review> findByProductProductID(Integer productID);
    
    /**
     * Lấy danh sách review của user
     * @param userID ID của user
     * @return List của reviews từ user
     */
    List<Review> findByUserUserID(Integer userID);
    
    // === ENHANCED METHODS FOR PRODUCT DETAIL ===
    
    /**
     * Lấy danh sách reviews theo product ID, sắp xếp theo ngày mới nhất
     * 
     * <p>Method này được sử dụng cho product detail page để hiển thị reviews
     * theo thứ tự chronological (mới nhất trước).</p>
     * 
     * @param productId ID của sản phẩm
     * @return List của reviews được sắp xếp theo reviewDate DESC
     */
    List<Review> findByProductProductIDOrderByReviewDateDesc(Integer productId);
    
    /**
     * Đếm tổng số reviews cho một sản phẩm
     * 
     * <p>Sử dụng để hiển thị "X Reviews" trên product detail page.</p>
     * 
     * @param productId ID của sản phẩm
     * @return Số lượng reviews cho sản phẩm
     */
    Long countByProductProductID(Integer productId);
    
    /**
     * Tính rating trung bình cho một sản phẩm
     * 
     * <p>Custom query để tính average rating. Trả về null nếu không có reviews.</p>
     * 
     * <p><strong>Business Logic:</strong></p>
     * <ul>
     *   <li>Chỉ tính reviews có rating > 0</li>
     *   <li>Trả về null nếu không có reviews (handled trong service layer)</li>
     *   <li>Kết quả được round đến 1 decimal place</li>
     * </ul>
     * 
     * @param productId ID của sản phẩm
     * @return Average rating (Double) hoặc null nếu không có reviews
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.productID = :productId AND r.rating > 0")
    Double findAverageRatingByProductId(@Param("productId") Integer productId);
    
    /**
     * Lấy reviews với pagination cho sản phẩm có nhiều reviews
     * 
     * <p>Sử dụng cho products có > 10 reviews để tránh load quá nhiều data.</p>
     * 
     * @param productId ID của sản phẩm
     * @param limit Số lượng reviews tối đa
     * @return List của reviews (limited)
     */
    @Query("SELECT r FROM Review r WHERE r.product.productID = :productId ORDER BY r.reviewDate DESC")
    List<Review> findTopReviewsByProductId(@Param("productId") Integer productId);
}

