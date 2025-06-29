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

    @GetMapping("/about")
    public String aboutpage() {
        return "about"; // about.html trong templates
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

    // ... rest of the code ...
}
