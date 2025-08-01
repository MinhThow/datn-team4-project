package com.java6.datn.Service;

import java.util.List;

import org.springframework.data.domain.Page;

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
    /**
     * Lấy danh sách ảnh của sản phẩm theo productID
     * @param productID ID sản phẩm
     * @return List<ProductImageDTO> danh sách ảnh
     */
    java.util.List<com.java6.datn.DTO.ProductImageDTO> getProductImagesByProductId(Integer productID);
    
    // === CRUD OPERATIONS ===
    
    /**
     * Lấy danh sách tất cả sản phẩm
     * 
     * @return List<ProductDTO> danh sách tất cả sản phẩm trong hệ thống
     */
    List<ProductDTO> getAllProducts();
    
    /**
     * Lấy thông tin chi tiết một sản phẩm theo ID
     * 
     * @param id ID của sản phẩm
     * @return ProductDTO thông tin sản phẩm
     * @throws RuntimeException nếu không tìm thấy sản phẩm
     */
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
    
    // === HOMEPAGE METHODS ===
    
    /**
     * Lấy danh sách sản phẩm bán chạy nhất
     * 
     * <p>Trả về các sản phẩm được coi là "best sellers" dựa trên logic nghiệp vụ.
     * Hiện tại sử dụng hardcoded list, có thể mở rộng thành query database
     * dựa trên số lượng bán, rating, hoặc các metrics khác.</p>
     * 
     * @param limit số lượng sản phẩm tối đa cần lấy
     * @return List<ProductDTO> danh sách sản phẩm bán chạy
     */
    List<ProductDTO> getBestSellers(int limit);
    
    /**
     * Lấy danh sách sản phẩm mới nhất
     * 
     * <p>Trả về các sản phẩm được coi là "new arrivals".
     * Hiện tại sử dụng hardcoded list, có thể mở rộng thành query
     * dựa trên ngày tạo, ngày import, hoặc flag "isNew".</p>
     * 
     * @param limit số lượng sản phẩm tối đa cần lấy
     * @return List<ProductDTO> danh sách sản phẩm mới
     */
    List<ProductDTO> getNewArrivals(int limit);
    
    /**
     * Lấy danh sách sản phẩm khuyến mãi
     * 
     * <p>Trả về các sản phẩm đang trong chương trình khuyến mãi "hot sales".
     * Hiện tại sử dụng hardcoded list, có thể mở rộng thành query
     * dựa trên discount percentage, promotion flags, hoặc special pricing.</p>
     * 
     * @param limit số lượng sản phẩm tối đa cần lấy
     * @return List<ProductDTO> danh sách sản phẩm khuyến mãi
     */
    List<ProductDTO> getHotSales(int limit);
    
    /**
     * Lấy sản phẩm nổi bật
     * 
     * <p>Trả về một sản phẩm được chọn làm "featured product" cho trang chủ.
     * Hiện tại hardcode ProductID=5, có thể mở rộng thành:
     * - Admin có thể set featured product
     * - Rotation tự động theo thời gian
     * - Dựa trên metrics như views, sales, ratings</p>
     * 
     * @return ProductDTO sản phẩm nổi bật, null nếu không có
     */
    ProductDTO getFeaturedProduct();
    
    // === SEARCH METHODS ===
    
    /**
     * Tìm kiếm sản phẩm theo từ khóa
     * 
     * <p>Thực hiện tìm kiếm sản phẩm dựa trên từ khóa người dùng nhập.
     * Search sẽ được thực hiện trên các field như tên sản phẩm, mô tả, etc.</p>
     * 
     * <p><strong>Search Strategy:</strong></p>
     * <ul>
     *   <li>Hiện tại: LIKE query với wildcards</li>
     *   <li>Tương lai: Full-text search, Elasticsearch, fuzzy matching</li>
     * </ul>
     * 
     * @param query từ khóa tìm kiếm
     * @return List<ProductDTO> danh sách sản phẩm khớp với từ khóa
     */
    List<ProductDTO> searchProducts(String query);

    // === PRODUCT DETAIL METHODS ===

    /**
     * Lấy danh sách sản phẩm liên quan
     *
     * <p>Trả về danh sách sản phẩm liên quan dựa trên sản phẩm hiện tại.
     * Logic recommendation:</p>
     * <ul>
     *   <li><strong>Primary:</strong> Sản phẩm cùng category</li>
     *   <li><strong>Secondary:</strong> Sản phẩm cùng price range</li>
     *   <li><strong>Future:</strong> Machine learning recommendations, user behavior, collaborative filtering</li>
     * </ul>
     *
     * <p><strong>Business Rules:</strong></p>
     * <ul>
     *   <li>Không bao gồm chính sản phẩm đang xem</li>
     *   <li>Ưu tiên sản phẩm cùng category</li>
     *   <li>Limit kết quả để tránh overwhelm user</li>
     *   <li>Sắp xếp theo relevance hoặc popularity</li>
     * </ul>
     *
     * <p><strong>Use Cases:</strong></p>
     * <ul>
     *   <li>Product detail page - "Related Products" section</li>
     *   <li>Cross-selling opportunities</li>
     *   <li>User engagement và discovery</li>
     *   <li>Increase average order value</li>
     * </ul>
     *
     * @param productId ID của sản phẩm hiện tại
     * @param limit số lượng sản phẩm liên quan tối đa (default: 4-6 sản phẩm)
     * @return List<ProductDTO> danh sách sản phẩm liên quan
     * @throws RuntimeException nếu không tìm thấy sản phẩm gốc
     *
     * @apiNote Được sử dụng bởi ProductController.getProductDetail()
     * @see #getProductById(Integer)
     */
    List<ProductDTO> getRelatedProducts(Integer productId, int limit);

    /**
     * Lấy danh sách sản phẩm liên quan với limit mặc định
     *
     * <p>Convenience method với limit mặc định = 4 sản phẩm.
     * Phù hợp cho hầu hết use cases của product detail page.</p>
     *
     * @param productId ID của sản phẩm hiện tại
     * @return List<ProductDTO> danh sách tối đa 4 sản phẩm liên quan
     * @see #getRelatedProducts(Integer, int)
     */
    default List<ProductDTO> getRelatedProducts(Integer productId) {
        return getRelatedProducts(productId, 4);
    }

    /**
     * Phân trang sản phẩm
     * @param page trang hiện tại
     * @param size số sản phẩm/trang
     * @return Page<ProductDTO>
     */
    Page<ProductDTO> getProductsPage(int page, int size);
}
