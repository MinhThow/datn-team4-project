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

