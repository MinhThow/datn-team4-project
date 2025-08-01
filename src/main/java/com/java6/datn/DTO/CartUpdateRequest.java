package com.java6.datn.DTO;

import lombok.Data;

@Data
public class CartUpdateRequest {
    private Integer cartItemID;
    private int quantity;
}