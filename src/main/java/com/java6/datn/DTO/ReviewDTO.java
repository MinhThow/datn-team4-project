package com.java6.datn.DTO;

import lombok.Data;

/**
 * ReviewDTO - Data Transfer Object cho Review entity
 * 
 * <p>DTO này được sử dụng để transfer review data giữa client và server:</p>
 * <ul>
 *   <li>API responses cho product detail pages</li>
 *   <li>Form submissions cho creating reviews</li>
 *   <li>Display data cho review lists</li>
 * </ul>
 * 
 * @author DATN Team 4
 * @version 1.0
 * @since 2025-01-16
 */
@Data
public class ReviewDTO {
    
    /**
     * ID của review (auto-generated)
     */
    private Integer reviewID;
    
    /**
     * ID của sản phẩm được review
     */
    private Integer productID;
    
    /**
     * ID của user tạo review
     */
    private Integer userID;
    
    /**
     * Tên của user (for display purposes)
     * Populated từ User entity khi load reviews
     */
    private String userName;
    
    /**
     * Rating từ 1-5 stars
     */
    private Integer rating;
    
    /**
     * Nội dung comment của review
     */
    private String comment;
    
    /**
     * Ngày tạo review (formatted string)
     * Format: "dd/MM/yyyy" hoặc "yyyy-MM-dd HH:mm:ss"
     */
    private String reviewDate;
}

