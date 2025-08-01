package com.java6.datn.Service;

import java.util.List;

import com.java6.datn.DTO.ProductSizeDTO;

public interface ProductSizeService {

    List<ProductSizeDTO> getProductSizes(Integer productId);
    
}
