package com.java6.datn.Service.Impl;

import com.java6.datn.Repository.ProductImageRepository;
import com.java6.datn.Service.ProductImageService;
import com.java6.datn.DTO.ProductImageDTO;
import com.java6.datn.Entity.ProductImage;
import com.java6.datn.Mapper.ProductImageMapper;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageMapper productImageMapper = new ProductImageMapper();
    private final ProductImageRepository productImageRepository;

    public ProductImageServiceImpl(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    @Override
    public List<ProductImageDTO> getProductImages(Integer productId) {
        return productImageRepository.findByProduct_ProductID(productId).stream()
                .map(productImageMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductImageDTO getProductImageById(Integer imageId) {
        ProductImage productImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Product image not found with ID: " + imageId));
        return productImageMapper.toDTO(productImage);
    }

    @Override
    public ProductImageDTO createProductImage(ProductImageDTO productImageDTO) {
        ProductImage productImage = productImageMapper.toEntity(productImageDTO);
        return productImageMapper.toDTO(productImageRepository.save(productImage));
    }

    @Override
    public ProductImageDTO updateProductImage(Integer imageId, ProductImageDTO productImageDTO) {
        ProductImage existingImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Product image not found with ID: " + imageId));
        
        // Update fields from DTO to existingImage
        existingImage.setImageUrl(productImageDTO.getImageUrl());
        existingImage.setMain(productImageDTO.isMain());
        // Assuming productID is handled by the mapper or not updated here directly

        return productImageMapper.toDTO(productImageRepository.save(existingImage));
    }
}
