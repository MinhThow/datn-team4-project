package com.java6.datn.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.java6.datn.dto.ProductDTO;
import com.java6.datn.entity.Product;

public interface ProductService {
    
    /**
     * Find products with optional filtering by keyword and category.
     * @param keyword search keyword for product name (optional)
     * @param categoryId category ID to filter by (optional)
     * @param pageable pagination information
     * @return Page of products matching the criteria
     */
    Page<Product> findAll(String keyword, Integer categoryId, Pageable pageable);
    
    /**
     * Find all products without filtering.
     * @param pageable pagination information
     * @return Page of all products
     */
    Page<Product> findAll(Pageable pageable);
    
    /**
     * Find product by ID.
     * @param id product ID
     * @return Product if found
     */
    Product findById(Integer id);
    
    /**
     * Find products with sale information for homepage.
     * @param keyword search keyword for product name (optional)
     * @param categoryId category ID to filter by (optional)
     * @param pageable pagination information
     * @return Page of ProductDTOs with sale information
     */
    Page<ProductDTO> findProductsWithSaleInfo(String keyword, Integer categoryId, Pageable pageable);
    
    /**
     * Get best selling products for homepage.
     * @param limit number of products to return
     * @return List of best selling ProductDTOs
     */
    List<ProductDTO> getBestSellers(int limit);
    
    /**
     * Get newest products for homepage.
     * @param limit number of products to return
     * @return List of newest ProductDTOs
     */
    List<ProductDTO> getNewArrivals(int limit);
    
    /**
     * Get products on sale for homepage.
     * @param limit number of products to return
     * @return List of ProductDTOs on sale
     */
    List<ProductDTO> getHotSales(int limit);
} 