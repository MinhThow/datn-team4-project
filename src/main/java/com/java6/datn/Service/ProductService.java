package com.java6.datn.Service;

import com.java6.datn.DTO.ProductDTO;
import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Integer id);
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(Integer id, ProductDTO productDTO);
    void deleteProduct(Integer id);
    
    // Homepage methods
    List<ProductDTO> getBestSellers(int limit);
    List<ProductDTO> getNewArrivals(int limit);
    List<ProductDTO> getHotSales(int limit);
    ProductDTO getFeaturedProduct();
    
    // Search methods
    List<ProductDTO> searchProducts(String query);
}

