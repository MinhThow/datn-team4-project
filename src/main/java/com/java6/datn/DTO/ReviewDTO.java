package com.java6.datn.DTO;

import lombok.Data;

@Data
public class ReviewDTO {

	private Integer reviewId;

	private Integer productId;

	private Integer userId;

	private String userName;
	
	private String productName; // ✅ Thêm dòng này
	
	private Integer rating;

	private String comment;

	private String reviewDate;
	private String imageUrl;
	private Integer orderId;

}
