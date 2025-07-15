package com.java6.datn.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemDTO {
    private Integer cartItemID;
    private Integer productID;         // 👈 thêm
    private Integer productSizeID;     // 👈 thêm
    private String productName;
    private String size;
    private String imageUrl;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal total;
}

