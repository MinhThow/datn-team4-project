package com.java6.datn.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/shop")
    public String home() {
        return "shop.html";
    }

    @GetMapping("/about")
    public String aboutpage() {
        return "shop-details.html"; // about.html trong templates
    }
}
