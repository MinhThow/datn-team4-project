package com.java6.datn.Controller;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.java6.datn.DTO.CategoryDTO;
import com.java6.datn.DTO.ProductDTO;
import com.java6.datn.Service.CategoryService;
import com.java6.datn.Service.ProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public HomeController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping({"/", "/home"})
    public String getHomePage(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (authorities.stream().anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()))) {
            model.addAttribute("email", username);
            return "redirect:/admin/dashboard";
        }
        try {
            Integer currentUserID = getCurrentUserID();
            log.debug("Current user ID: {}", currentUserID);

            List<ProductDTO> bestSellers = productService.getBestSellers(8);
            log.info("Loaded {} best sellers", bestSellers.size());
            List<ProductDTO> newArrivals = productService.getNewArrivals(8);
            log.info("Loaded {} new arrivals", newArrivals.size());
            List<ProductDTO> hotSales = productService.getHotSales(8);
            log.info("Loaded {} hot sales", hotSales.size());
            ProductDTO featuredProduct = productService.getFeaturedProduct();
            log.info("Featured product: {}", featuredProduct != null ? featuredProduct.getName() : "None");

            List<CategoryDTO> categories = categoryService.getAllCategories();
            log.info("Loaded {} categories", categories.size());

            model.addAttribute("bestSellers", bestSellers);
            model.addAttribute("newArrivals", newArrivals);
            model.addAttribute("hotSales", hotSales);
            model.addAttribute("featuredProduct", featuredProduct);
            model.addAttribute("categories", categories);

            return "index";
        } catch (Exception e) {
            log.error("Error loading homepage data", e);
            return "error";
        }
    }

    private Integer getCurrentUserID() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                log.debug("User is authenticated: {}", auth.getName());
                return null;
            }
            log.debug("User is not authenticated or is anonymous");
            return null;
        } catch (Exception e) {
            log.warn("Error getting current user ID", e);
            return null;
        }
    }
}