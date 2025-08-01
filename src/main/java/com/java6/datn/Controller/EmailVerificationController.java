package com.java6.datn.Controller;

import com.java6.datn.Entity.User;
import com.java6.datn.Repository.UserRepository;
import com.java6.datn.Service.EmailService;
import com.java6.datn.Service.VerificationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class EmailVerificationController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/verify-email")
    public ResponseEntity<?> sendEmail(Authentication auth, HttpServletRequest request) {
    	try {
    	    String email = auth.getName();
    	    Optional<User> optUser = userRepo.findByEmail(email);

    	    if (optUser.isEmpty()) return ResponseEntity.notFound().build();

    	    User user = optUser.get();
    	    String token = tokenService.createToken(user);
    	    String url = request.getRequestURL().toString().replace("/verify-email", "")
    	            + "/verify-email/confirm?token=" + token;

    	    String subject = "Xác minh email của bạn";
    	    String content = String.format("Xin chào %s,\n\nNhấn vào liên kết dưới đây để xác minh email:\n%s", user.getName(), url);
    	    emailService.sendEmail(user.getEmail(), subject, content);

    	    return ResponseEntity.ok("Đã gửi email xác minh");

    	} catch (Exception e) {
    	    e.printStackTrace(); // ← để xem rõ lỗi trong console
    	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    	                         .body("Lỗi gửi email: " + e.getMessage());
    	}

    }

    @GetMapping("/verify-email/confirm")
    public String confirm(@RequestParam("token") String token, Model model) {
        boolean result = tokenService.verifyToken(token);
        model.addAttribute("success", result); // gắn vào để Thymeleaf xử lý
        return "verify-success"; // ✅ trả về trang HTML thay vì ResponseEntity
    }


}
