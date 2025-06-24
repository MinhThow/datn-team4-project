package com.java6.datn.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long orderID;
    private Long userID;
    private BigDecimal total;
    private String status;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private Long paymentMethodID;
}

