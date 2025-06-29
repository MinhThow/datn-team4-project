package com.java6.datn.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java6.datn.DTO.ProductDTO;
import com.java6.datn.DTO.ReviewDTO;
import com.java6.datn.Service.ProductService;
import com.java6.datn.Service.ReviewService;

/**
 * ProductController - REST API Controller cho quản lý sản phẩm
 * 
 * <p>Controller này cung cấp các REST API endpoints để:</p>
 * <ul>
 *   <li>Lấy danh sách tất cả sản phẩm</li>
 *   <li>Lấy chi tiết một sản phẩm theo ID</li>
 *   <li>Tạo sản phẩm mới</li>
 *   <li>Cập nhật thông tin sản phẩm</li>
 *   <li>Xóa sản phẩm</li>
 *   <li>Tìm kiếm sản phẩm theo từ khóa</li>
 * </ul>
 * 
 * <p><strong>Base URL:</strong> /api/products</p>
 * 
 * <p><strong>Endpoints:</strong></p>
 * <ul>
 *   <li>GET /api/products - Lấy tất cả sản phẩm</li>
 *   <li>GET /api/products/{id} - Lấy sản phẩm theo ID</li>
 *   <li>POST /api/products - Tạo sản phẩm mới</li>
 *   <li>PUT /api/products/{id} - Cập nhật sản phẩm</li>
 *   <li>DELETE /api/products/{id} - Xóa sản phẩm</li>
 *   <li>GET /api/products/search?query=... - Tìm kiếm sản phẩm</li>
 * </ul>
 * 
 * @author DATN Team 4
 * @version 1.0
 * @since 2025-01-16
 */
@RestController
@RequestMapping("/api/products")
//@CrossOrigin(origins = "*") // Có thể enable khi cần CORS support
public class ProductController {

    /**
     * Service xử lý logic nghiệp vụ liên quan đến sản phẩm
     * Được inject thông qua constructor injection
     */
    private final ProductService productService;

    /**
     * Service xử lý logic liên quan đến đánh giá sản phẩm
     * Được inject thông qua constructor injection
     */
    private final ReviewService reviewService;

    /**
     * Constructor injection cho ProductService và ReviewService
     * 
     * @param productService Service xử lý logic sản phẩm
     * @param reviewService Service xử lý logic đánh giá sản phẩm
     */
    public ProductController(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
    }

    /**
     * Lấy danh sách tất cả sản phẩm
     * 
     * <p>Endpoint này trả về tất cả sản phẩm có trong hệ thống.
     * Được sử dụng bởi:</p>
     * <ul>
     *   <li>Trang quản lý admin</li>
     *   <li>API clients cần full product list</li>
     *   <li>Mobile app hoặc external integrations</li>
     * </ul>
     * 
     * @return List&lt;ProductDTO&gt; danh sách tất cả sản phẩm
     * 
     * @apiNote GET /api/products
     * @apiExample
     * <pre>
     * GET /api/products
     * Response: [
     *   {
     *     "productID": 1,
     *     "name": "Áo Sơ Mi Nam",
     *     "price": 299000,
     *     "categoryID": 1,
     *     "image": "img/product/product-1.jpg"
     *   }
     * ]
     * </pre>
     */
    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.getAllProducts();
    }

    /**
     * Lấy chi tiết một sản phẩm theo ID
     * 
     * <p>Endpoint này trả về thông tin chi tiết của một sản phẩm cụ thể.
     * Được sử dụng cho:</p>
     * <ul>
     *   <li>Trang chi tiết sản phẩm</li>
     *   <li>Form chỉnh sửa sản phẩm</li>
     *   <li>API validation</li>
     * </ul>
     * 
     * @param id ID của sản phẩm cần lấy thông tin
     * @return ProductDTO thông tin chi tiết sản phẩm
     * @throws RuntimeException nếu không tìm thấy sản phẩm với ID đã cho
     * 
     * @apiNote GET /api/products/{id}
     * @apiExample
     * <pre>
     * GET /api/products/1
     * Response: {
     *   "productID": 1,
     *   "name": "Áo Sơ Mi Nam",
     *   "price": 299000,
     *   "categoryID": 1,
     *   "image": "img/product/product-1.jpg"
     * }
     * </pre>
     */
    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    /**
     * Tạo sản phẩm mới
     * 
     * <p>Endpoint này cho phép tạo một sản phẩm mới trong hệ thống.
     * Thường được sử dụng bởi:</p>
     * <ul>
     *   <li>Admin panel để thêm sản phẩm</li>
     *   <li>Bulk import tools</li>
     *   <li>External integrations</li>
     * </ul>
     * 
     * <p><strong>Validation:</strong> ProductDTO sẽ được validate theo các rules
     * được định nghĩa trong entity và service layer.</p>
     * 
     * @param productDTO Thông tin sản phẩm mới cần tạo
     * @return ProductDTO thông tin sản phẩm đã được tạo (bao gồm ID được generate)
     * 
     * @apiNote POST /api/products
     * @apiExample
     * <pre>
     * POST /api/products
     * Request Body: {
     *   "name": "Áo Thun Mới",
     *   "price": 199000,
     *   "categoryID": 2,
     *   "image": "img/product/new-product.jpg"
     * }
     * Response: {
     *   "productID": 11,
     *   "name": "Áo Thun Mới",
     *   "price": 199000,
     *   "categoryID": 2,
     *   "image": "img/product/new-product.jpg"
     * }
     * </pre>
     */
    @PostMapping
    public ProductDTO create(@RequestBody ProductDTO productDTO) {
        return productService.createProduct(productDTO);
    }

    /**
     * Cập nhật thông tin sản phẩm
     * 
     * <p>Endpoint này cho phép cập nhật thông tin của một sản phẩm đã tồn tại.
     * Thường được sử dụng để:</p>
     * <ul>
     *   <li>Chỉnh sửa thông tin sản phẩm từ admin panel</li>
     *   <li>Cập nhật giá, hình ảnh, mô tả</li>
     *   <li>Bulk update operations</li>
     * </ul>
     * 
     * @param id ID của sản phẩm cần cập nhật
     * @param productDTO Thông tin mới của sản phẩm
     * @return ProductDTO thông tin sản phẩm sau khi cập nhật
     * @throws RuntimeException nếu không tìm thấy sản phẩm với ID đã cho
     * 
     * @apiNote PUT /api/products/{id}
     * @apiExample
     * <pre>
     * PUT /api/products/1
     * Request Body: {
     *   "name": "Áo Sơ Mi Nam (Updated)",
     *   "price": 349000,
     *   "categoryID": 1,
     *   "image": "img/product/product-1-updated.jpg"
     * }
     * Response: {
     *   "productID": 1,
     *   "name": "Áo Sơ Mi Nam (Updated)",
     *   "price": 349000,
     *   "categoryID": 1,
     *   "image": "img/product/product-1-updated.jpg"
     * }
     * </pre>
     */
    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable Integer id, @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO);
    }

    /**
     * Xóa sản phẩm
     * 
     * <p>Endpoint này cho phép xóa một sản phẩm khỏi hệ thống.
     * <strong>Lưu ý:</strong> Thao tác này có thể ảnh hưởng đến:</p>
     * <ul>
     *   <li>Đơn hàng đã có sản phẩm này</li>
     *   <li>Giỏ hàng của users</li>
     *   <li>Review và rating</li>
     * </ul>
     * 
     * <p>Nên cân nhắc sử dụng soft delete thay vì hard delete.</p>
     * 
     * @param id ID của sản phẩm cần xóa
     * @throws RuntimeException nếu không tìm thấy sản phẩm với ID đã cho
     * 
     * @apiNote DELETE /api/products/{id}
     * @apiExample
     * <pre>
     * DELETE /api/products/1
     * Response: 204 No Content
     * </pre>
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        productService.deleteProduct(id);
    }

    /**
     * Tìm kiếm sản phẩm theo từ khóa
     * 
     * <p>Endpoint này cung cấp chức năng tìm kiếm sản phẩm dựa trên từ khóa.
     * Tìm kiếm sẽ được thực hiện trên các field:</p>
     * <ul>
     *   <li>Tên sản phẩm (name)</li>
     *   <li>Mô tả sản phẩm (description) - nếu có</li>
     *   <li>Có thể mở rộng thêm các field khác</li>
     * </ul>
     * 
     * <p><strong>Được sử dụng bởi:</strong></p>
     * <ul>
     *   <li>Search box trên header website</li>
     *   <li>Mobile search functionality</li>
     *   <li>Advanced search filters</li>
     *   <li>Auto-complete suggestions</li>
     * </ul>
     * 
     * <p><strong>Search Logic:</strong> Hiện tại sử dụng LIKE query với wildcards,
     * có thể mở rộng thành full-text search hoặc Elasticsearch sau này.</p>
     * 
     * @param query Từ khóa tìm kiếm (required)
     * @return List&lt;ProductDTO&gt; danh sách sản phẩm khớp với từ khóa
     * 
     * @apiNote GET /api/products/search?query={keyword}
     * @apiExample
     * <pre>
     * GET /api/products/search?query=áo
     * Response: [
     *   {
     *     "productID": 1,
     *     "name": "Áo Sơ Mi Nam",
     *     "price": 299000,
     *     "categoryID": 1,
     *     "image": "img/product/product-1.jpg"
     *   },
     *   {
     *     "productID": 2,
     *     "name": "Áo Thun Nữ",
     *     "price": 199000,
     *     "categoryID": 2,
     *     "image": "img/product/product-2.jpg"
     *   }
     * ]
     * </pre>
     * 
     * @see ProductService#searchProducts(String)
     */
    @GetMapping("/search")
    public List<ProductDTO> searchProducts(@RequestParam String query) {
        // Delegate search logic đến service layer
        // Service sẽ handle validation, formatting, và database query
        return productService.searchProducts(query);
    }

    // === PRODUCT DETAIL OPERATIONS ===

    /**
     * Lấy thông tin chi tiết sản phẩm cho trang product detail
     * 
     * <p>Endpoint này trả về thông tin comprehensive cho trang chi tiết sản phẩm bao gồm:</p>
     * <ul>
     *   <li>Thông tin cơ bản của sản phẩm</li>
     *   <li>Danh sách reviews và ratings</li>
     *   <li>Thống kê rating (average, total reviews)</li>
     *   <li>Danh sách sản phẩm liên quan (cùng category)</li>
     * </ul>
     * 
     * <p><strong>Use Cases:</strong></p>
     * <ul>
     *   <li>Hiển thị trang shop-details.html</li>
     *   <li>AJAX calls cho dynamic content loading</li>
     *   <li>Mobile app product detail screen</li>
     *   <li>SEO-friendly product information</li>
     * </ul>
     * 
     * <p><strong>Response Structure:</strong></p>
     * <pre>
     * {
     *   "product": ProductDTO,
     *   "reviews": List&lt;ReviewDTO&gt;,
     *   "averageRating": Double,
     *   "totalReviews": Integer,
     *   "relatedProducts": List&lt;ProductDTO&gt;
     * }
     * </pre>
     * 
     * @param id ID của sản phẩm cần lấy chi tiết
     * @return Map&lt;String, Object&gt; chứa tất cả thông tin cần thiết cho product detail page
     * @throws RuntimeException nếu không tìm thấy sản phẩm với ID đã cho
     * 
     * @apiNote GET /api/products/{id}/detail
     * @apiExample
     * <pre>
     * GET /api/products/1/detail
     * Response: {
     *   "product": {
     *     "productID": 1,
     *     "name": "Áo Sơ Mi Nam",
     *     "price": 299000,
     *     "categoryID": 1,
     *     "image": "img/product/product-1.jpg"
     *   },
     *   "reviews": [
     *     {
     *       "reviewID": 1,
     *       "rating": 5,
     *       "comment": "Sản phẩm tuyệt vời!",
     *       "userName": "Nguyễn Văn A",
     *       "reviewDate": "15/01/2025"
     *     }
     *   ],
     *   "averageRating": 4.5,
     *   "totalReviews": 8,
     *   "relatedProducts": [...]
     * }
     * </pre>
     * 
     * @see ProductService#getProductById(Integer)
     * @see ReviewService#getReviewsByProductId(Integer)
     * @see ReviewService#getAverageRating(Integer)
     * @see ReviewService#getTotalReviews(Integer)
     * @see ProductService#getRelatedProducts(Integer)
     */
    @GetMapping("/{id}/detail")
    public Map<String, Object> getProductDetail(@PathVariable Integer id) {
        // Step 1: Validate input
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Product ID must be positive integer");
        }

        // Step 2: Lấy thông tin sản phẩm
        ProductDTO product = productService.getProductById(id);

        // Step 3: Lấy reviews và rating statistics
        List<ReviewDTO> reviews = reviewService.getReviewsByProductId(id);
        Double averageRating = reviewService.getAverageRating(id);
        Integer totalReviews = reviewService.getTotalReviews(id);

        // Step 4: Lấy related products (cùng category)
        List<ProductDTO> relatedProducts = productService.getRelatedProducts(id);

        // Step 5: Build response object
        Map<String, Object> response = new HashMap<>();
        response.put("product", product);
        response.put("reviews", reviews);
        response.put("averageRating", averageRating);
        response.put("totalReviews", totalReviews);
        response.put("relatedProducts", relatedProducts);

        return response;
    }

    /**
     * Lấy reviews của sản phẩm với pagination
     * 
     * <p>Endpoint này hỗ trợ load more reviews hoặc pagination cho trang chi tiết sản phẩm.
     * Được sử dụng khi:</p>
     * <ul>
     *   <li>Sản phẩm có quá nhiều reviews</li>
     *   <li>Implement "Load More" button</li>
     *   <li>AJAX pagination</li>
     * </ul>
     * 
     * @param id ID của sản phẩm
     * @param page Số trang (default = 0)
     * @param size Số reviews per page (default = 10)
     * @return List&lt;ReviewDTO&gt; danh sách reviews theo pagination
     * 
     * @apiNote GET /api/products/{id}/reviews?page=0&size=10
     * @apiExample
     * <pre>
     * GET /api/products/1/reviews?page=0&size=5
     * Response: [
     *   {
     *     "reviewID": 1,
     *     "rating": 5,
     *     "comment": "Tuyệt vời!",
     *     "userName": "User A",
     *     "reviewDate": "15/01/2025"
     *   }
     * ]
     * </pre>
     */
    @GetMapping("/{id}/reviews")
    public List<ReviewDTO> getProductReviews(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return reviewService.getReviewsByProductIdWithPagination(id, page, size);
    }

    /**
     * Thêm review mới cho sản phẩm
     * 
     * <p>Endpoint này cho phép user tạo review mới cho sản phẩm.
     * Business rules:</p>
     * <ul>
     *   <li>User chỉ có thể review 1 lần cho mỗi sản phẩm</li>
     *   <li>Rating phải từ 1-5 sao</li>
     *   <li>Comment không được để trống</li>
     *   <li>User phải đã login</li>
     * </ul>
     * 
     * @param id ID của sản phẩm
     * @param reviewDTO Thông tin review mới
     * @return ReviewDTO review đã được tạo
     * @throws RuntimeException nếu user đã review sản phẩm này rồi
     * 
     * @apiNote POST /api/products/{id}/reviews
     * @apiExample
     * <pre>
     * POST /api/products/1/reviews
     * Request Body: {
     *   "userID": 1,
     *   "rating": 5,
     *   "comment": "Sản phẩm tuyệt vời!"
     * }
     * Response: {
     *   "reviewID": 9,
     *   "productID": 1,
     *   "userID": 1,
     *   "rating": 5,
     *   "comment": "Sản phẩm tuyệt vời!",
     *   "userName": "Nguyễn Văn A",
     *   "reviewDate": "16/01/2025"
     * }
     * </pre>
     */
    @PostMapping("/{id}/reviews")
    public ReviewDTO addProductReview(@PathVariable Integer id, @RequestBody ReviewDTO reviewDTO) {
        // Set product ID từ path parameter
        reviewDTO.setProductID(id);
        
        // Delegate to review service
        return reviewService.createReview(reviewDTO);
    }
}

