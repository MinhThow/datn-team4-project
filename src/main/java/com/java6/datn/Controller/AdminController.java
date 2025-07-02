package com.java6.datn.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/user")
    public String user() {
        return "admin/user/list";
    }

    @GetMapping("/category")
    public String category() {
        return "admin/category/list";
    }

    @GetMapping("/product")
    public String admin() {
        return "admin/product/list";
    }

    @GetMapping("/method")
    public String method() {
        return "admin/method/list";
    }

    @GetMapping("/order")
    public String order() {
        return "admin/order/list";
    }
}
