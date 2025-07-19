package com.java6.datn.Mapper;

import com.java6.datn.DTO.ProductImageDTO;
import com.java6.datn.Entity.Product;
import com.java6.datn.Entity.ProductImage;

public class ProductImageMapper {

    public ProductImageDTO toDTO(ProductImage productImage) {
        if (productImage == null) {
            return null;
        }
        ProductImageDTO dto = new ProductImageDTO();
        dto.setImageID(productImage.getImageID());
        dto.setProductID(productImage.getProduct() != null ? productImage.getProduct().getProductID() : null);
        dto.setImageUrl(productImage.getImageUrl());
        dto.setMain(productImage.isMain());
        return dto;
    }

    public ProductImage toEntity(ProductImageDTO productImageDTO) {
        if (productImageDTO == null) {
            return null;
        }
        ProductImage entity = new ProductImage();
        entity.setImageID(productImageDTO.getImageID());
        if (productImageDTO.getProductID() != null) {
            Product product = new Product();
            product.setProductID(productImageDTO.getProductID());
            entity.setProduct(product);
        }
        entity.setImageUrl(productImageDTO.getImageUrl());
        entity.setMain(productImageDTO.isMain());
        return entity;
    }
}