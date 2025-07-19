package com.java6.datn.Mapper;

import com.java6.datn.DTO.ProductSizeDTO;
import com.java6.datn.Entity.Product;
import com.java6.datn.Entity.ProductSize;

public class ProductSizeMapper {

    public ProductSizeDTO toDTO(ProductSize productSize) {
        if (productSize == null) {
            return null;
        }
        ProductSizeDTO dto = new ProductSizeDTO();
        dto.setProductSizeID(productSize.getProductSizeID());
        dto.setProductID(productSize.getProductSizeID());
        dto.setSize(productSize.getSize());
        dto.setStock(productSize.getStock());
        System.out.println("ProductSizeDTO ::"+dto.toString());
        return dto;
    }
}