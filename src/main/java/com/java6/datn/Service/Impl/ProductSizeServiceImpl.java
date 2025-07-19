package com.java6.datn.Service.Impl;

import com.java6.datn.Entity.ProductSize;
import com.java6.datn.Repository.ProductSizeRepository;
import com.java6.datn.Service.ProductSizeService;
import com.java6.datn.DTO.ProductSizeDTO;
import org.springframework.stereotype.Service;
import com.java6.datn.Mapper.ProductSizeMapper;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductSizeServiceImpl implements ProductSizeService {

    private final ProductSizeRepository productSizeRepository;
    private final ProductSizeMapper productSizeMapper = new ProductSizeMapper();

    public ProductSizeServiceImpl(ProductSizeRepository productSizeRepository) {
        this.productSizeRepository = productSizeRepository;
    }

    @Override
    public List<ProductSizeDTO> getProductSizes(Integer productId) {
        List<ProductSize> data = productSizeRepository.findByProduct_ProductID(productId);
        System.out.println("data:{}"+data);
        return productSizeRepository.findByProduct_ProductID(productId).stream()
                .map(productSizeMapper::toDTO)
                .collect(Collectors.toList());
    }
}
