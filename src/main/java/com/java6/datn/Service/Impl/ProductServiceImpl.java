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
        if (productDTO.getOldPrice() == null) {
            product.setOldPrice(productDTO.getPrice()); // Set old price same as current price for new products
        }
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
        existingProduct.setOldPrice(existingProduct.getPrice());
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
        // Lấy tất cả sản phẩm và sắp xếp theo giá giảm dần (tạm thời)
        return productRepository.findAll().stream()
                .sorted((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()))
                .limit(limit)
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getNewArrivals(int limit) {
        // Lấy sản phẩm và sắp xếp theo ID giảm dần (ID cao = sản phẩm mới)
        return productRepository.findAll().stream()
                .sorted((p1, p2) -> p2.getProductID().compareTo(p1.getProductID()))
                .limit(limit)
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getHotSales(int limit) {
        // Lấy sản phẩm có giá cũ > giá mới (đang giảm giá)
        return productRepository.findAll().stream()
                .filter(p -> p.getOldPrice() != null && p.getOldPrice().compareTo(p.getPrice()) > 0)
                .limit(limit)
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getFeaturedProduct() {
        // Lấy sản phẩm có giá cao nhất làm featured product
        return productRepository.findAll().stream()
                .max((p1, p2) -> p1.getPrice().compareTo(p2.getPrice()))
                .map(ProductMapper::toDTO)
                .orElse(null);
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

    // === PRODUCT DETAIL METHODS ===

    @Override
    public List<ProductDTO> getRelatedProducts(Integer productId, int limit) {
        // Step 1: Validate input
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive integer");
        }
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive");
        }

        // Step 2: Lấy thông tin sản phẩm hiện tại
        Product currentProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // Step 3: Tìm sản phẩm cùng category (excluding current product)
        List<Product> relatedProducts = productRepository.findAll()
                .stream()
                .filter(product -> {
                    // Exclude chính sản phẩm đang xem
                    if (product.getProductID().equals(productId)) {
                        return false;
                    }
                    
                    // Ưu tiên sản phẩm cùng category
                    if (currentProduct.getCategory() != null && product.getCategory() != null) {
                        return currentProduct.getCategory().getCategoryID()
                                .equals(product.getCategory().getCategoryID());
                    }
                    
                    return false;
                })
                .limit(limit)
                .collect(Collectors.toList());

        // Step 4: Nếu không đủ sản phẩm cùng category, thêm sản phẩm khác
        if (relatedProducts.size() < limit) {
            int remaining = limit - relatedProducts.size();
            
            List<Product> additionalProducts = productRepository.findAll()
                    .stream()
                    .filter(product -> {
                        // Exclude current product và các products đã có
                        if (product.getProductID().equals(productId)) {
                            return false;
                        }
                        
                        // Exclude products đã được thêm vào related list
                        return relatedProducts.stream()
                                .noneMatch(related -> related.getProductID().equals(product.getProductID()));
                    })
                    .limit(remaining)
                    .collect(Collectors.toList());
                    
            relatedProducts.addAll(additionalProducts);
        }

        // Step 5: Convert to DTOs và return
        return relatedProducts.stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }
}

