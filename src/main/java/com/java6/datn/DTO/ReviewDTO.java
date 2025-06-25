package com.java6.datn.DTO;

import lombok.Data;

@Data
public class ReviewDTO {
    private Integer reviewID;
    private Integer productID;
    private Integer userID;
    private Integer rating;
    private String comment;
    private String reviewDate; // trả chuỗi ngày
}

