package com.java6.datn.Mapper;

import com.java6.datn.DTO.ProductDTO;
import com.java6.datn.Entity.Product;

public class ProductMapper {

    public static ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductID(product.getProductID());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setImage(product.getImage());
        dto.setSize(product.getSize());
        if (product.getCategory() != null) {
            dto.setCategoryID(product.getCategory().getCategoryID());
            dto.setCategoryName(product.getCategory().getName());
        }
        return dto;
    }

    public static Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setProductID(dto.getProductID());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImage(dto.getImage());
        product.setSize(dto.getSize());
        // Category sẽ set bên ngoài Service vì cần truy vấn CategoryRepository
        return product;
    }
}

