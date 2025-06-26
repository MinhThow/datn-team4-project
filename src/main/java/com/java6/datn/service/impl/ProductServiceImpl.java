package com.java6.datn.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.java6.datn.dto.ProductDTO;
import com.java6.datn.entity.Product;
import com.java6.datn.entity.SaleDetail;
import com.java6.datn.repository.ProductRepository;
import com.java6.datn.repository.SaleDetailRepository;
import com.java6.datn.repository.specification.ProductSpecification;
import com.java6.datn.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private SaleDetailRepository saleDetailRepository;

    @Override
    public Page<Product> findAll(String keyword, Integer categoryId, Pageable pageable) {
        // Use ProductSpecification to build dynamic query
        Specification<Product> spec = ProductSpecification.getSpecs(keyword, categoryId);
        return productRepository.findAll(spec, pageable);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }
    
    @Override
    public Page<ProductDTO> findProductsWithSaleInfo(String keyword, Integer categoryId, Pageable pageable) {
        // Get products using specification
        Specification<Product> spec = ProductSpecification.getSpecs(keyword, categoryId);
        Page<Product> productPage = productRepository.findAll(spec, pageable);
        
        // Convert to DTOs with sale information
        List<ProductDTO> productDTOs = convertToProductDTOs(productPage.getContent());
        
        return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
    }
    
    @Override
    public List<ProductDTO> getBestSellers(int limit) {
        // For now, return top products by ID (can be enhanced with actual sales data later)
        Pageable pageable = PageRequest.of(0, limit, Sort.by("productID").ascending());
        Page<Product> products = productRepository.findAll(pageable);
        return convertToProductDTOs(products.getContent());
    }
    
    @Override
    public List<ProductDTO> getNewArrivals(int limit) {
        // Get newest products by ID (assuming higher ID = newer)
        Pageable pageable = PageRequest.of(0, limit, Sort.by("productID").descending());
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductDTO> dtos = convertToProductDTOs(products.getContent());
        
        // Mark as new
        dtos.forEach(dto -> dto.setNew(true));
        return dtos;
    }
    
    @Override
    public List<ProductDTO> getHotSales(int limit) {
        try {
            // Get all products first and check if any have sales
            Pageable pageable = PageRequest.of(0, limit, Sort.by("productID").ascending());
            Page<Product> products = productRepository.findAll(pageable);
            
            if (products.isEmpty()) {
                return List.of();
            }
            
            List<ProductDTO> dtos = convertToProductDTOs(products.getContent());
            
            // Mark products with sale prices as hot sales
            dtos = dtos.stream()
                    .filter(dto -> dto.getSalePrice() != null)
                    .collect(Collectors.toList());
            
            return dtos.size() > limit ? dtos.subList(0, limit) : dtos;
        } catch (Exception e) {
            // Return empty list if there's any error
            return List.of();
        }
    }
    
    private List<ProductDTO> convertToProductDTOs(List<Product> products) {
        if (products.isEmpty()) {
            return List.of();
        }
        
        // Get product IDs
        List<Integer> productIds = products.stream()
                .map(Product::getProductID)
                .collect(Collectors.toList());
        
        Map<Integer, SaleDetail> salesMap = Map.of();
        try {
            // Get active sales for these products
            List<SaleDetail> activeSales = saleDetailRepository.findActiveSalesByProductIds(productIds);
            salesMap = activeSales.stream()
                    .collect(Collectors.toMap(SaleDetail::getProductID, sale -> sale));
        } catch (Exception e) {
            // If sales query fails, continue without sales data
            salesMap = Map.of();
        }
        
        final Map<Integer, SaleDetail> finalSalesMap = salesMap;
        
        // Convert to DTOs
        return products.stream()
                .filter(product -> product.getStock() > 0) // Only include products in stock
                .map(product -> {
                    SaleDetail sale = finalSalesMap.get(product.getProductID());
                    return new ProductDTO(
                            product.getProductID(),
                            product.getName(),
                            product.getDescription(),
                            product.getPrice(),
                            sale != null ? sale.getSalePrice() : null,
                            product.getStock(),
                            product.getImage(),
                            product.getCategoryID(),
                            product.getSize()
                    );
                })
                .collect(Collectors.toList());
    }
} 