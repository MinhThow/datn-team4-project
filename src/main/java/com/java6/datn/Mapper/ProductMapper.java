package com.java6.datn.Mapper;

import com.java6.datn.DTO.ProductDTO;
import com.java6.datn.Entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {


    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }
        ProductDTO dto = new ProductDTO();
        dto.setProductID(product.getProductID());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setOldPrice(product.getOldPrice());
        dto.setStock(product.getStock());
        if (product.getCategory() != null) {
            dto.setCategoryID(product.getCategory().getCategoryID());
            dto.setCategoryName(product.getCategory().getName());
        }
        return dto;
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }
        Product product = new Product();
        product.setProductID(dto.getProductID());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setOldPrice(dto.getOldPrice());
        product.setStock(dto.getStock());
        // Category will be set outside Service as it needs CategoryRepository
        // productImages and productSizes are not mapped from DTO to Entity here
        return product;
    }
}