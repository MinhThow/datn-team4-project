package com.java6.datn.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.java6.datn.DTO.CategoryDTO;
import com.java6.datn.DTO.ProductDTO;
import com.java6.datn.Service.CartService;
import com.java6.datn.Service.CategoryService;
import com.java6.datn.Service.ProductService;

import lombok.extern.slf4j.Slf4j;

/**
 * HomeController - Controller chính cho trang chủ của ứng dụng e-commerce
 * 
 * <p>Controller này chịu trách nhiệm xử lý các request đến trang chủ và chuẩn bị
 * tất cả dữ liệu cần thiết để hiển thị:</p>
 * 
 * <ul>
 *   <li>Sản phẩm bán chạy (Best Sellers)</li>
 *   <li>Sản phẩm mới (New Arrivals)</li>
 *   <li>Sản phẩm khuyến mãi (Hot Sales)</li>
 *   <li>Sản phẩm nổi bật (Featured Product)</li>
 *   <li>Danh mục sản phẩm (Categories)</li>
 *   <li>Thông tin giỏ hàng của user hiện tại</li>
 * </ul>
 * 
 * <p><strong>Luồng hoạt động:</strong></p>
 * <ol>
 *   <li>Nhận request từ "/" hoặc "/home"</li>
 *   <li>Lấy thông tin user hiện tại (nếu đã đăng nhập)</li>
 *   <li>Gọi các service để lấy dữ liệu sản phẩm và danh mục</li>
 *   <li>Tính toán thông tin giỏ hàng</li>
 *   <li>Truyền tất cả dữ liệu vào Model</li>
 *   <li>Trả về template "index.html" để render</li>
 * </ol>
 * 
 * @author DATN Team 4
 * @version 1.0
 * @since 2025-01-16
 */
@Slf4j
@Controller
public class HomeController {

    // === DEPENDENCY INJECTION ===
    
    /**
     * Service xử lý logic nghiệp vụ liên quan đến sản phẩm
     * Cung cấp các method: getBestSellers, getNewArrivals, getHotSales, getFeaturedProduct
     */
    private final ProductService productService;
    
    /**
     * Service xử lý logic nghiệp vụ liên quan đến danh mục sản phẩm
     * Cung cấp method: getAllCategories
     */
    private final CategoryService categoryService;
    
    /**
     * Service xử lý logic nghiệp vụ liên quan đến giỏ hàng
     * Cung cấp các method: getCartItemCount, getCartTotal
     */
    private final CartService cartService;

    /**
     * Constructor injection - Spring tự động inject các dependency
     * 
     * @param productService  Service xử lý sản phẩm
     * @param categoryService Service xử lý danh mục
     * @param cartService     Service xử lý giỏ hàng
     */
    public HomeController(ProductService productService, CategoryService categoryService, CartService cartService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.cartService = cartService;
    }

    // === MAIN ENDPOINT ===

    /**
     * Xử lý request GET đến trang chủ
     * 
     * <p>Method này là endpoint chính của trang chủ, xử lý cả "/" và "/home".
     * Nó thực hiện các bước sau:</p>
     * 
     * <ol>
     *   <li><strong>Authentication Check:</strong> Kiểm tra user hiện tại có đăng nhập không</li>
     *   <li><strong>Product Data Loading:</strong> Lấy 4 loại sản phẩm khác nhau từ database</li>
     *   <li><strong>Category Loading:</strong> Lấy tất cả danh mục để hiển thị menu</li>
     *   <li><strong>Cart Calculation:</strong> Tính số lượng và tổng tiền giỏ hàng</li>
     *   <li><strong>Model Population:</strong> Đưa tất cả dữ liệu vào Model</li>
     *   <li><strong>Template Rendering:</strong> Trả về template index.html</li>
     * </ol>
     * 
     * <p><strong>Dữ liệu được truyền vào Model:</strong></p>
     * <ul>
     *   <li>bestSellers: List&lt;ProductDTO&gt; - 8 sản phẩm bán chạy nhất</li>
     *   <li>newArrivals: List&lt;ProductDTO&gt; - 8 sản phẩm mới nhất</li>
     *   <li>hotSales: List&lt;ProductDTO&gt; - 8 sản phẩm khuyến mãi</li>
     *   <li>featuredProduct: ProductDTO - 1 sản phẩm nổi bật</li>
     *   <li>categories: List&lt;CategoryDTO&gt; - Tất cả danh mục</li>
     *   <li>cartItemCount: int - Số lượng item trong giỏ hàng</li>
     *   <li>cartTotalPrice: BigDecimal - Tổng tiền giỏ hàng</li>
     * </ul>
     * 
     * @param model Spring Model object để truyền dữ liệu sang view
     * @return String tên template ("index") để render trang chủ
     * 
     * @see ProductService#getBestSellers(int)
     * @see ProductService#getNewArrivals(int)
     * @see ProductService#getHotSales(int)
     * @see ProductService#getFeaturedProduct()
     * @see CategoryService#getAllCategories()
     * @see CartService#getCartItemCount(Integer)
     * @see CartService#getCartTotal(Integer)
     */
    @GetMapping({"/", "/home"})
    public String getHomePage(Model model) {
        try {
            // === STEP 1: AUTHENTICATION CHECK ===
            // Lấy thông tin user hiện tại (null nếu chưa đăng nhập)
            // Điều này quan trọng để tính toán giỏ hàng chính xác
            Integer currentUserID = getCurrentUserID();
            log.debug("Current user ID: {}", currentUserID);

            // === STEP 2: PRODUCT DATA LOADING ===
            // Lấy các loại sản phẩm khác nhau để hiển thị trên trang chủ
            // Mỗi loại giới hạn 8 sản phẩm để không làm chậm trang
            
            List<ProductDTO> bestSellers = productService.getBestSellers(8);
            log.info("Loaded {} best sellers", bestSellers.size());
            
            List<ProductDTO> newArrivals = productService.getNewArrivals(8);
            log.info("Loaded {} new arrivals", newArrivals.size());
            
            List<ProductDTO> hotSales = productService.getHotSales(8);
            log.info("Loaded {} hot sales", hotSales.size());
            
            // Sản phẩm nổi bật - chỉ 1 sản phẩm duy nhất
            ProductDTO featuredProduct = productService.getFeaturedProduct();
            log.info("Featured product: {}", featuredProduct != null ? featuredProduct.getName() : "None");

            // === STEP 3: CATEGORY LOADING ===
            // Lấy tất cả danh mục để hiển thị menu navigation
            List<CategoryDTO> categories = categoryService.getAllCategories();
            log.info("Loaded {} categories", categories.size());
            
            // === STEP 4: CART CALCULATION ===
            // Tính toán thông tin giỏ hàng cho user hiện tại
            // Nếu chưa đăng nhập thì trả về 0
            int cartItemCount = cartService.getCartItemCount(currentUserID);
            BigDecimal cartTotalPrice = cartService.getCartTotal(currentUserID);
            log.debug("Cart info - Items: {}, Total: {}", cartItemCount, cartTotalPrice);

            // === STEP 5: MODEL POPULATION ===
            // Đưa tất cả dữ liệu đã chuẩn bị vào Model
            // Dữ liệu này sẽ được Thymeleaf sử dụng trong template
            model.addAttribute("bestSellers", bestSellers);
            model.addAttribute("newArrivals", newArrivals);
            model.addAttribute("hotSales", hotSales);
            model.addAttribute("featuredProduct", featuredProduct);
            model.addAttribute("categories", categories);
            model.addAttribute("cartItemCount", cartItemCount);
            model.addAttribute("cartTotalPrice", cartTotalPrice);

            // === STEP 6: TEMPLATE RENDERING ===
            // Trả về tên template "index" -> Spring sẽ render index.html
            return "index";
            
        } catch (Exception e) {
            // === ERROR HANDLING ===
            // Log lỗi chi tiết để debug
            log.error("Error loading homepage data", e);
            // Trong production nên redirect đến trang lỗi đẹp hơn
            return "error";
        }
    }

    // === HELPER METHODS ===

    /**
     * Lấy ID của user hiện tại từ Spring Security Context
     * 
     * <p>Method này kiểm tra xem user có đăng nhập không bằng cách:</p>
     * <ol>
     *   <li>Lấy Authentication object từ SecurityContextHolder</li>
     *   <li>Kiểm tra authentication có null không</li>
     *   <li>Kiểm tra user có authenticated không</li>
     *   <li>Kiểm tra không phải anonymous user</li>
     * </ol>
     * 
     * <p><strong>Lưu ý:</strong> Hiện tại method này chưa implement việc extract
     * user ID từ authentication object. Cần implement sau khi có User entity.</p>
     * 
     * @return Integer ID của user hiện tại, null nếu chưa đăng nhập hoặc là guest
     * 
     * @see SecurityContextHolder#getContext()
     * @see Authentication#isAuthenticated()
     */
    private Integer getCurrentUserID() {
        try {
            // Lấy thông tin authentication từ Spring Security
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            // Kiểm tra user có đăng nhập không
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                // TODO: Implement user ID extraction từ authentication
                // Hiện tại return null vì chưa có User entity hoàn chỉnh
                // Khi implement User authentication, cần:
                // 1. Cast auth.getPrincipal() về UserDetails custom
                // 2. Lấy user ID từ UserDetails đó
                log.debug("User is authenticated: {}", auth.getName());
                return null; // Tạm thời return null
            }
            
            // User chưa đăng nhập hoặc là anonymous
            log.debug("User is not authenticated or is anonymous");
            return null;
            
        } catch (Exception e) {
            // Nếu có lỗi trong quá trình check authentication
            log.warn("Error getting current user ID", e);
            return null;
        }
    }
} 