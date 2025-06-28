package com.java6.datn.Service;

import java.util.List;

import com.java6.datn.DTO.ProductDTO;

/**
 * ProductService - Interface định nghĩa các service methods cho quản lý sản phẩm
 * 
 * <p>Interface này định nghĩa tất cả các operations liên quan đến sản phẩm trong hệ thống e-commerce:</p>
 * 
 * <ul>
 *   <li><strong>CRUD Operations:</strong> Create, Read, Update, Delete sản phẩm</li>
 *   <li><strong>Homepage Methods:</strong> Lấy dữ liệu cho trang chủ (best sellers, new arrivals, etc.)</li>
 *   <li><strong>Search Methods:</strong> Tìm kiếm sản phẩm theo từ khóa</li>
 * </ul>
 * 
 * <p><strong>Implementation:</strong> ProductServiceImpl</p>
 * <p><strong>Repository Layer:</strong> ProductRepository</p>
 * <p><strong>Entity:</strong> Product</p>
 * <p><strong>DTO:</strong> ProductDTO</p>
 * 
 * @author DATN Team 4
 * @version 1.0
 * @since 2025-01-16
 * @see com.java6.datn.Service.Impl.ProductServiceImpl
 * @see com.java6.datn.Repository.ProductRepository
 * @see com.java6.datn.Entity.Product
 * @see com.java6.datn.DTO.ProductDTO
 */
public interface ProductService {
    
    // === CRUD OPERATIONS ===
    
    /**
     * Lấy danh sách tất cả sản phẩm
     * 
     * @return List&lt;ProductDTO&gt; danh sách tất cả sản phẩm trong hệ thống
     */
    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(Integer id);
    
    /**
     * Tạo sản phẩm mới
     * 
     * @param productDTO thông tin sản phẩm mới
     * @return ProductDTO sản phẩm đã được tạo (bao gồm ID)
     */
    ProductDTO createProduct(ProductDTO productDTO);
    
    /**
     * Cập nhật thông tin sản phẩm
     * 
     * @param id ID của sản phẩm cần cập nhật
     * @param productDTO thông tin mới của sản phẩm
     * @return ProductDTO sản phẩm sau khi cập nhật
     * @throws RuntimeException nếu không tìm thấy sản phẩm
     */
    ProductDTO updateProduct(Integer id, ProductDTO productDTO);
    
    /**
     * Xóa sản phẩm
     * 
     * @param id ID của sản phẩm cần xóa
     * @throws RuntimeException nếu không tìm thấy sản phẩm
     */
    void deleteProduct(Integer id);



