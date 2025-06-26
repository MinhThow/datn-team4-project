package com.java6.datn.service;

import com.java6.datn.dto.ProductDTO;
import com.java6.datn.dto.ProductDTO_V2;
import com.java6.datn.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Integer id);
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(Integer id, ProductDTO productDTO);
    void deleteProduct(Integer id);


    Page<Product> findAll(String keyword, Integer categoryId, Pageable pageable);


    Page<Product> findAll(Pageable pageable);


    Product findById(Integer id);


    Page<ProductDTO_V2> findProductsWithSaleInfo(String keyword, Integer categoryId, Pageable pageable);


    List<ProductDTO_V2> getBestSellers(int limit);


    List<ProductDTO_V2> getNewArrivals(int limit);


    List<ProductDTO_V2> getHotSales(int limit);
}

