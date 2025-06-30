package com.java6.datn.DTO;

import com.java6.datn.Entity.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Integer orderID;
    private Integer userID;
    private String name;
    private BigDecimal total;
    private String status;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private Integer paymentMethodID;
    private String paymentMethodName;
    private List<OrderItemDTO> orderItemsDTO;
}