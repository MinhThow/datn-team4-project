package com.java6.datn.Controller;

import com.java6.datn.Repository.ProductRepository;
import com.java6.datn.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
//    OrderRepository orderRepository;

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

    // ✅ API TRẢ VỀ JSON THỐNG KÊ
    @GetMapping("/api/dashboard/stats")
    @ResponseBody // bắt buộc để trả JSON khi dùng @Controller
    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new LinkedHashMap<>();

        // Gọi đúng qua biến đã autowired
        stats.put("Sản phẩm", productRepository.count());
        stats.put("Người dùng", userRepository.count());
//        stats.put("Đơn hàng", orderRepository.count());

        return stats;
    }
}
