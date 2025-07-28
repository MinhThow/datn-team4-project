package com.java6.datn.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDisplayDTO {
    private Integer orderId;
    private String productName;
    private String imageUrl;
    private String size;
    private Integer quantity;
    private BigDecimal price;
    private String status;
    private String formattedDate;
 // OrderDisplayDTO.java
    private String badgeColor;
    private String shippingAddress;
    private String note;
    private String paymentMethodName;
    private BigDecimal totalPrice;

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }


}