package com.java6.datn.service.impl;

import com.java6.datn.dto.ProductDTO;
import com.java6.datn.dto.ProductDTO_V2;
import com.java6.datn.entity.Category;
import com.java6.datn.entity.Product;
import com.java6.datn.entity.SaleDetail;
import com.java6.datn.mapper.ProductMapper;
import com.java6.datn.repository.CategoryRepository;
import com.java6.datn.repository.ProductRepository;
import com.java6.datn.repository.SaleDetailRepository;
import com.java6.datn.repository.specification.ProductSpecification;
import com.java6.datn.service.ProductService;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SaleDetailRepository saleDetailRepository;
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,SaleDetailRepository saleDetailRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.saleDetailRepository = saleDetailRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ProductMapper.toDTO(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = ProductMapper.toEntity(productDTO);
        if (productDTO.getCategoryID() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryID())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        return ProductMapper.toDTO(productRepository.save(product));
    }

    @Override
    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setStock(productDTO.getStock());
        existingProduct.setImage(productDTO.getImage());
        existingProduct.setSize(productDTO.getSize());
        if (productDTO.getCategoryID() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryID())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingProduct.setCategory(category);
        }

        return ProductMapper.toDTO(productRepository.save(existingProduct));
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }







    //--------------------

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
    public Page<ProductDTO_V2> findProductsWithSaleInfo(String keyword, Integer categoryId, Pageable pageable) {
        // Get products using specification
        Specification<Product> spec = ProductSpecification.getSpecs(keyword, categoryId);
        Page<Product> productPage = productRepository.findAll(spec, pageable);

        // Convert to DTOs with sale information
        List<ProductDTO_V2> productDTOs = convertToProductDTOs(productPage.getContent());

        return new PageImpl<>(productDTOs, pageable, productPage.getTotalElements());
    }

    @Override
    public List<ProductDTO_V2> getBestSellers(int limit) {
        // For now, return top products by ID (can be enhanced with actual sales data later)
        Pageable pageable = PageRequest.of(0, limit, Sort.by("productID").ascending());
        Page<Product> products = productRepository.findAll(pageable);
        return convertToProductDTOs(products.getContent());
    }

    @Override
    public List<ProductDTO_V2> getNewArrivals(int limit) {
        // Get newest products by ID (assuming higher ID = newer)
        Pageable pageable = PageRequest.of(0, limit, Sort.by("productID").descending());
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductDTO_V2> dtos = convertToProductDTOs(products.getContent());

        // Mark as new
        dtos.forEach(dto -> dto.setNew(true));
        return dtos;
    }

    @Override
    public List<ProductDTO_V2> getHotSales(int limit) {
        try {
            // Get all products first and check if any have sales
            Pageable pageable = PageRequest.of(0, limit, Sort.by("productID").ascending());
            Page<Product> products = productRepository.findAll(pageable);

            if (products.isEmpty()) {
                return List.of();
            }

            List<ProductDTO_V2> dtos = convertToProductDTOs(products.getContent());

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

    private List<ProductDTO_V2> convertToProductDTOs(List<Product> products) {
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
                    return new ProductDTO_V2(
                            product.getProductID(),
                            product.getName(),
                            product.getDescription(),
                            product.getPrice(),
                            sale != null ? sale.getSalePrice() : null,
                            product.getStock(),
                            product.getImage(),
                            product.getCategory().getCategoryID(),
                            product.getSize()
                    );
                })
                .collect(Collectors.toList());
    }


}

