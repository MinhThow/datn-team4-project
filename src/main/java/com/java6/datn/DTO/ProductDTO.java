package com.java6.datn.DTO;

import java.math.BigDecimal;

import lombok.Data;


// trao đổi data giữa client và server
@Data
public class ProductDTO {
    private Integer productID;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal oldPrice;
    private Integer stock;
    private String image;
    private String size;
    private Integer categoryID;
    private String categoryName;
}

