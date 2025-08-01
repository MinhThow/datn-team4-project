package com.java6.datn.DTO;

// dữ liệu dùng chung trong request và respone
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Integer productID;
    private Integer productSizeID;
    private String productName;
    private String size;
    private Integer quantity;
    private BigDecimal price;
}

