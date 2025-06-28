package com.java6.datn.Service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.java6.datn.DTO.ProductDTO;
import com.java6.datn.Entity.Category;
import com.java6.datn.Entity.Product;
import com.java6.datn.Mapper.ProductMapper;
import com.java6.datn.Repository.CategoryRepository;
import com.java6.datn.Repository.ProductRepository;
import com.java6.datn.Service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
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

    // Homepage methods implementation
    @Override
    public List<ProductDTO> getBestSellers(int limit) {
        // Tạm thời return hardcoded best sellers theo database analysis
        // ProductIDs: 1 (Áo Sơ Mi Nam), 2 (Áo Thun Nữ), 3 (Quần Jean Nam)
        List<Integer> bestSellerIds = List.of(1, 2, 3, 6, 7, 8, 9, 10);
        return bestSellerIds.stream()
                .limit(limit)
                .map(id -> {
                    try {
                        return getProductById(id);
                    } catch (RuntimeException e) {
                        return null;
                    }
                })
                .filter(product -> product != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getNewArrivals(int limit) {
        // Tạm thời return hardcoded new arrivals
        // ProductIDs: 6 (Áo Khoác Bomber), 7 (Đầm Maxi), 10 (Áo Len Nữ)
        List<Integer> newArrivalIds = List.of(6, 7, 10, 8, 9, 1, 2, 3);
        return newArrivalIds.stream()
                .limit(limit)
                .map(id -> {
                    try {
                        return getProductById(id);
                    } catch (RuntimeException e) {
                        return null;
                    }
                })
                .filter(product -> product != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getHotSales(int limit) {
        // Tạm thời return hardcoded hot sales (giá tốt)
        // ProductIDs: 4 (Chân Váy 380K), 8 (Quần Short 320K), 2 (Áo Thun 280K)
        List<Integer> hotSaleIds = List.of(4, 8, 2, 10, 1, 3, 6, 7);
        return hotSaleIds.stream()
                .limit(limit)
                .map(id -> {
                    try {
                        return getProductById(id);
                    } catch (RuntimeException e) {
                        return null;
                    }
                })
                .filter(product -> product != null)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getFeaturedProduct() {
        // Tạm thời return sản phẩm cao cấp nhất: Túi Xách Nữ Da Thật (ProductID=5, 1.2M VND)
        try {
            return getProductById(5);
        } catch (RuntimeException e) {
            // Fallback to first available product
            List<ProductDTO> allProducts = getAllProducts();
            return allProducts.isEmpty() ? null : allProducts.get(0);
        }
    }

    @Override
    public List<ProductDTO> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllProducts();
        }
        
        String searchQuery = query.trim().toLowerCase();
        
        return productRepository.findAll()
                .stream()
                .filter(product -> {
                    // Tìm kiếm theo tên sản phẩm
                    boolean nameMatch = product.getName() != null && 
                                      product.getName().toLowerCase().contains(searchQuery);
                    
                    // Tìm kiếm theo mô tả
                    boolean descriptionMatch = product.getDescription() != null && 
                                             product.getDescription().toLowerCase().contains(searchQuery);
                    
                    // Tìm kiếm theo tên category
                    boolean categoryMatch = product.getCategory() != null && 
                                          product.getCategory().getName() != null &&
                                          product.getCategory().getName().toLowerCase().contains(searchQuery);
                    
                    return nameMatch || descriptionMatch || categoryMatch;
                })
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }
}

