package com.java6.datn.Service;

import java.util.List;

import com.java6.datn.DTO.ProductImageDTO;

public interface ProductImageService {

    List<ProductImageDTO> getProductImages(Integer productId);

    ProductImageDTO getProductImageById(Integer imageId);

    ProductImageDTO createProductImage(ProductImageDTO productImageDTO);

    ProductImageDTO updateProductImage(Integer imageId, ProductImageDTO productImageDTO);
} 