package com.java6.datn.Controller;

import com.java6.datn.DTO.ProductDTO;
import com.java6.datn.DTO.ReviewDTO;
import com.java6.datn.Service.ProductService;
import com.java6.datn.Service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {
    private static final Logger log = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    static String getString(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (authorities.stream().anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()))) {
            model.addAttribute("email", username);
            return "redirect:/admin/dashboard";
        } else {
            if (!username.isEmpty() && !"anonymousUser".equals(username)) {
                model.addAttribute("email", username);
            }
            return "index";
        }
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Integer id, Model model) {
        try {
            ProductDTO product = productService.getProductById(id);
            List<ReviewDTO> reviews = reviewService.getReviewsByProductId(id);
            Double averageRating = reviewService.getAverageRating(id);
            Integer totalReviews = reviewService.getTotalReviews(id);
            List<ProductDTO> relatedProducts = productService.getRelatedProducts(id);
            log.info("relatedProducts:: {}", relatedProducts);

            model.addAttribute("product", product);
            model.addAttribute("reviews", reviews);
            model.addAttribute("averageRating", averageRating);
            model.addAttribute("totalReviews", totalReviews);
            model.addAttribute("relatedProducts", relatedProducts);

            model.addAttribute("fullStars", (int) Math.floor(averageRating));
            model.addAttribute("hasHalfStar", (averageRating % 1) >= 0.5);
            model.addAttribute("emptyStars", 5 - (int) Math.ceil(averageRating));

            return "shop-details";
        } catch (Exception e) {
            model.addAttribute("error", "Product not found");
            return "redirect:/shop";
        }
    }

    //Removed cart related API endpoint
    /*@PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam Integer productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            @RequestParam String size,
            Model model) {
        // ...
    }*/
}