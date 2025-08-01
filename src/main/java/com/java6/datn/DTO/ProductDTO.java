package com.java6.datn.DTO;

import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;


// trao đổi data giữa client và server
@Data
@ToString
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
    private String imageUrl;
    private java.util.List<ProductImageDTO> productImages;
}
