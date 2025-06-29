package com.java6.datn.Controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java6.datn.DTO.ProductDTO;
import com.java6.datn.DTO.ReviewDTO;
import com.java6.datn.DTO.CartItemDTO;
import com.java6.datn.Service.ProductService;
import com.java6.datn.Service.ReviewService;
import com.java6.datn.Service.CartItemService;
import com.java6.datn.Service.CartService;

/**
 * WebController - Controller cho các trang web frontend
 * 
 * <p>Controller này handle các requests cho frontend pages và integrate
 * với backend services để provide dynamic content.</p>
 * 
 * @author DATN Team 4
 * @version 2.0
 * @since 2025-01-16
 */
@Controller
public class WebController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private CartItemService cartItemService;
    
    @Autowired
    private CartService cartService;

    @GetMapping("/shop")
    public String home() {
        return "shop.html";
    }

    @GetMapping("/about")
    public String aboutpage() {
        return "about"; // about.html trong templates
    }

    @GetMapping("/login")
    public String showLogin() {
        return "Login_Register.html"; // trả file login.html
    }

    /**
     * Product detail page
     * 
     * <p>Hiển thị trang chi tiết sản phẩm với dynamic content từ backend APIs.
     * Page này integrate với ProductService và ReviewService để provide:</p>
     * <ul>
     *   <li>Product information (name, price, description, image)</li>
     *   <li>Reviews và ratings</li>
     *   <li>Related products recommendations</li>
     *   <li>Add to cart functionality</li>
     * </ul>
     * 
     * @param id Product ID từ URL path
     * @param model Thymeleaf model để pass data to template
     * @return String template name (shop-details)
     * 
     * @apiNote URL pattern: /product/{id}
     * @apiExample
     * <pre>
     * GET /product/1 -> Product detail page cho product ID 1
     * GET /product/5 -> Product detail page cho product ID 5
     * </pre>
     */
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Integer id, Model model) {
        try {
            // Step 1: Lấy product information
            ProductDTO product = productService.getProductById(id);
            
            // Step 2: Lấy reviews và rating statistics
            List<ReviewDTO> reviews = reviewService.getReviewsByProductId(id);
            Double averageRating = reviewService.getAverageRating(id);
            Integer totalReviews = reviewService.getTotalReviews(id);
            
            // Step 3: Lấy related products
            List<ProductDTO> relatedProducts = productService.getRelatedProducts(id);
            
            // Step 4: Add data to Thymeleaf model
            model.addAttribute("product", product);
            model.addAttribute("reviews", reviews);
            model.addAttribute("averageRating", averageRating);
            model.addAttribute("totalReviews", totalReviews);
            model.addAttribute("relatedProducts", relatedProducts);
            
            // Step 5: Add helper attributes cho template
            model.addAttribute("fullStars", (int) Math.floor(averageRating));
            model.addAttribute("hasHalfStar", (averageRating % 1) >= 0.5);
            model.addAttribute("emptyStars", 5 - (int) Math.ceil(averageRating));
            
            return "shop-details"; // shop-details.html template
            
        } catch (Exception e) {
            // Step 6: Error handling - redirect to shop page
            model.addAttribute("error", "Product not found");
            return "redirect:/shop";
        }
    }

    /**
     * Add product to cart via AJAX
     * 
     * <p>Handle add to cart requests từ product detail page. 
     * Endpoint này support both authenticated users và guest users.</p>
     * 
     * <p><strong>Features:</strong></p>
     * <ul>
     *   <li>Product validation (exists, available, in stock)</li>
     *   <li>Size validation (required, valid for product)</li>
     *   <li>Quantity validation (positive, <= stock)</li>
     *   <li>Merge quantities if product already in cart</li>
     *   <li>Update cart counter</li>
     * </ul>
     * 
     * @param productId ID của product cần add to cart
     * @param quantity Số lượng cần add (default: 1)
     * @param size Kích cỡ được chọn (required)
     * @param model Thymeleaf model cho error handling
     * @return ResponseEntity<Map<String, Object>> JSON response với status và data
     * 
     * @apiNote POST /cart/add
     * @apiExample
     * <pre>
     * POST /cart/add
     * Content-Type: application/x-www-form-urlencoded
     * 
     * productId=1&quantity=2&size=M
     * 
     * Response:
     * {
     *   "success": true,
     *   "message": "Product added to cart successfully",
     *   "cartItemCount": 5,
     *   "cartTotal": 150.00
     * }
     * </pre>
     */
    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam Integer productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            @RequestParam String size,
            Model model) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Step 1: Validate input parameters
            if (productId == null || quantity <= 0 || size == null || size.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Invalid input parameters");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Step 2: Validate product exists và available
            ProductDTO product = productService.getProductById(productId);
            if (product == null) {
                response.put("success", false);
                response.put("message", "Product not found");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Step 3: Validate stock availability
            if (product.getStock() < quantity) {
                response.put("success", false);
                response.put("message", "Insufficient stock. Available: " + product.getStock());
                return ResponseEntity.badRequest().body(response);
            }
            
            // Step 4: Validate size (check if size exists in product.size)
            String[] availableSizes = product.getSize().split(", ");
            boolean sizeValid = false;
            for (String availableSize : availableSizes) {
                if (availableSize.trim().equalsIgnoreCase(size.trim())) {
                    sizeValid = true;
                    break;
                }
            }
            
            if (!sizeValid) {
                response.put("success", false);
                response.put("message", "Invalid size selected");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Step 5: Create cart item (for now, use userID = 1 as demo)
            // TODO: Get actual userID from session/authentication
            Integer userId = 1; // Demo user ID
            
            CartItemDTO cartItem = new CartItemDTO();
            cartItem.setUserID(userId);
            cartItem.setProductID(productId);
            cartItem.setQuantity(quantity);
            cartItem.setSize(size); // Set selected size
            
            // Step 6: Add to cart (merge if exists)
            CartItemDTO savedCartItem = cartItemService.createCartItem(cartItem);
            
            // Step 7: Get updated cart statistics
            int cartItemCount = cartService.getCartItemCount(userId);
            double cartTotal = cartService.getCartTotal(userId).doubleValue();
            
            // Step 8: Success response
            response.put("success", true);
            response.put("message", "Product added to cart successfully");
            response.put("cartItemCount", cartItemCount);
            response.put("cartTotal", cartTotal);
            response.put("productName", product.getName());
            response.put("selectedSize", size);
            response.put("addedQuantity", quantity);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            // Step 9: Error handling
            response.put("success", false);
            response.put("message", "Failed to add product to cart: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

/// login thành công
}
