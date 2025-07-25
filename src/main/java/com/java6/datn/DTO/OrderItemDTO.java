package com.java6.datn.DTO;

// dữ liệu dùng chung trong request và respone
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Integer productId;
    private Integer productSizeId;
    private String productName;
    private String size;
    private Integer quantity;
    private BigDecimal price;
}

