package com.java6.datn.Controller;

import ch.qos.logback.core.model.Model;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
//    @GetMapping("/shop")
//    public String home() {
//        return "shop";
//    }

    @GetMapping("/about")
    public String aboutpage() {
        return "about"; // about.html trong templates
    }

    @GetMapping("/home")
    public String homepage() {
        return "index"; // about.html trong templates
    }






}

/// login thành công

