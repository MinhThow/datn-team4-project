package com.java6.datn.controller;

import org.springframework.security.core.context.SecurityContextHolder;
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
        return "index"; // about.html trong templates
    }


    @GetMapping("/login")
    public String showLogin() {
        return "Login_Register.html"; // trả file login.html
    }

/// login thành công
}
