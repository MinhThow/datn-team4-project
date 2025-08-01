package com.java6.datn.Controller;

import com.java6.datn.DTO.ProductDTO;
import com.java6.datn.Service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShopController {

    private final ProductService productService;

    public ShopController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/shop")
    public String shopPage(Model model, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 6;
        Page<ProductDTO> products = productService.getProductsPage(page, pageSize);
        model.addAttribute("products", products);
        return "shop"; // trả template shop.html
    }

    @GetMapping("/cart")
    public String shopDetailPage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "shopping-cart"; //
    }

    @GetMapping("/checkout")
    public String chitiet(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "checkout"; //
    }





}
