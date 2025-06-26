package com.java6.datn.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Integer orderID;
    private Integer userID;
    private BigDecimal total;
    private String status;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private Integer paymentMethodID;
}

