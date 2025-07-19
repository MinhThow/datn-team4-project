package com.java6.datn.DTO;
import lombok.Data;

@Data
public class ProductImageDTO {
    private Integer imageID;
    private Integer productID;
    private String imageUrl;
    private boolean isMain;
}
