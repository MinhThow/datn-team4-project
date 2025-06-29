package com.java6.datn.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartItemDTO {
    private Integer cartItemID;
    private Integer userID;
    private Integer productID;
    private Integer quantity;
    private String size; // Added for size selection support
    private LocalDateTime addedAt;
}

