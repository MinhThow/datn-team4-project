package com.java6.datn.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long orderItemID;
    private Long orderID;
    private Long productID;
    private Integer quantity;
    private BigDecimal price;
}
