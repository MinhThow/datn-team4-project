package com.java6.datn.DTO;

import lombok.Data;

@Data
public class ProductSizeDTO {
    private Integer productSizeID;
    private Integer productID;
    private String size;
    private Integer stock;
}
