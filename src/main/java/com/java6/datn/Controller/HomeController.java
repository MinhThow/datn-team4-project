package com.java6.datn.Controller;

import com.java6.datn.DTO.CategoryDTO;
import com.java6.datn.DTO.ProductDTO;
import com.java6.datn.Service.CartService;
import com.java6.datn.Service.CategoryService;
import com.java6.datn.Service.ProductService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CartService cartService;

    public HomeController(ProductService productService, CategoryService categoryService, CartService cartService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.cartService = cartService;
    }

    @GetMapping({"/", "/home"})
    public String getHomePage(Model model) {
        try {
            // Get current user ID (null if not authenticated)
            Integer currentUserID = getCurrentUserID();

            // Get product data for homepage
            List<ProductDTO> bestSellers = productService.getBestSellers(8);
            log.info("Best Sellers: {}", bestSellers);
            List<ProductDTO> newArrivals = productService.getNewArrivals(8);
            log.info("New Arrivals: {}", newArrivals);
            List<ProductDTO> hotSales = productService.getHotSales(8);
            log.info("Hot Sales: {}", hotSales);
            ProductDTO featuredProduct = productService.getFeaturedProduct();
            log.info("Featured Product: {}", featuredProduct);

            // Get categories for navigation
            List<CategoryDTO> categories = categoryService.getAllCategories();
            log.info("Categories: {}", categories);
            // Get cart data (0 if not logged in)
            int cartItemCount = cartService.getCartItemCount(currentUserID);
            BigDecimal cartTotalPrice = cartService.getCartTotal(currentUserID);

            // Add data to model
            model.addAttribute("bestSellers", bestSellers);
            model.addAttribute("newArrivals", newArrivals);
            model.addAttribute("hotSales", hotSales);
            model.addAttribute("featuredProduct", featuredProduct);
            model.addAttribute("categories", categories);
            model.addAttribute("cartItemCount", cartItemCount);
            model.addAttribute("cartTotalPrice", cartTotalPrice);

            return "index";
        } catch (Exception e) {
            // Log error and return error page or redirect
            e.printStackTrace();
            return "error";
        }
    }

    private Integer getCurrentUserID() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                // TODO: Implement user ID extraction from authentication
                // For now, return null (guest user)
                return null;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
} 