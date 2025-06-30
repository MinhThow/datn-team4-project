package com.java6.datn.Controller;

import com.java6.datn.Service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShopController {

    private final ProductService productService;

    public ShopController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/shop")
    public String shopPage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "shop"; // trả template shop.html
    }




}


