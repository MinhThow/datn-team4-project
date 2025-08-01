package com.java6.datn.Mapper;

import com.java6.datn.DTO.ProductDTO;
import com.java6.datn.Entity.Product;
import com.java6.datn.Entity.ProductImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Slf4j
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
        // Map danh sách ảnh sang DTO
        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            java.util.List<com.java6.datn.DTO.ProductImageDTO> imageDTOs = new java.util.ArrayList<>();
            for (com.java6.datn.Entity.ProductImage img : product.getProductImages()) {
                com.java6.datn.DTO.ProductImageDTO imgDTO = new com.java6.datn.DTO.ProductImageDTO();
                imgDTO.setImageID(img.getImageID());
                imgDTO.setProductID(product.getProductID());
                imgDTO.setImageUrl(img.getImageUrl());
                imgDTO.setMain(img.isMain());
                imageDTOs.add(imgDTO);
            }
            dto.setProductImages(imageDTOs);
            // Set imageUrl đại diện (nếu có)
            dto.setImageUrl(imageDTOs.get(0).getImageUrl());
        } else {
            dto.setProductImages(new java.util.ArrayList<>());
            dto.setImage("img/product/product-1.jpg");
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