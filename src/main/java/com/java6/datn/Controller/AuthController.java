package com.java6.datn.Controller;

import com.java6.datn.DTO.UserRegisterDTO;
import com.java6.datn.Entity.User;
import com.java6.datn.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserRegisterDTO());
        return "Login_Register";
    }

    // Xử lý form đăng ký
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") UserRegisterDTO dto) {

        // Kiểm tra email đã tồn tại
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return "redirect:/register?error=email";
        }

        // Kiểm tra mật khẩu và xác nhận
        if (dto.getPassword() == null || !dto.getPassword().equals(dto.getConfirmPassword())) {
            return "redirect:/register";
        }


        // Tạo user mới
        User user = new User();
        user.setEmail(dto.getEmail());             // Tên Đăng nhập
        user.setName(dto.getUsername());           // Tên người dùng
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("customer");                  // Role mặc định là khách hàng

        userRepository.save(user);

        // Chuyển về login sau khi đăng ký thành công
        return "redirect:/login?success=true";
    }


    @GetMapping("/login-register")
    public String loginRegisterPage(Model model) {
        model.addAttribute("user", new UserRegisterDTO());
        return "Login_Register";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new UserRegisterDTO());
        return "Login_Register";
    }




}
