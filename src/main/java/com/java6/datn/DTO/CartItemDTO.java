package com.java6.datn.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartItemDTO {
    private Long cartItemID;
    private Long userID;
    private Long productID;
    private Integer quantity;
    private LocalDateTime addedAt;
}

