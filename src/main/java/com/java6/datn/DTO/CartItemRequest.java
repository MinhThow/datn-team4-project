package com.java6.datn.DTO;


import lombok.Data;

@Data
public class CartItemRequest {
    private int userId;
    private int productId;
    private int productSizeId;
    private int quantity;
}
