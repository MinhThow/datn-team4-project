package com.java6.datn.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Integer orderItemID;
    private Integer orderID;
    private Integer productID;
    private Integer quantity;
    private BigDecimal price;
}
