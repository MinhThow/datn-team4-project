package com.java6.datn.Security;


import com.java6.datn.Entity.User;

import com.java6.datn.Repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Set;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    

    public CustomLoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
       
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        // Lấy thông tin người dùng từ UserDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);

        

        

        // Nếu là USER thì set greeting vào session
        if (user != null && (roles.contains("ROLE_CUSTOMER") || roles.contains("customer"))) {
            request.getSession().setAttribute("fullName", user.getName());
            request.getSession().setAttribute("greetingMessage", getGreeting());
        }

        // Phân quyền chuyển hướng
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else if (roles.contains("ROLE_CUSTOMER") || roles.contains("customer")) {
            response.sendRedirect("/home");
        } else {
            response.sendRedirect("/login?error=role");
        }
    }

    private String getGreeting() {
        LocalTime now = LocalTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        int hour = now.getHour();

        if (hour >= 5 && hour < 12) {
            return "Good morning";
        } else if (hour >= 12 && hour < 17) {
            return "Good afternoon";
        } else if (hour >= 17 && hour < 21) {
            return "Good evening";
        } else {
            return "Good night";
        }
    }
    private String extractBrowser(String userAgent) {
        if (userAgent == null) return "Unknown";

        userAgent = userAgent.toLowerCase();

        if (userAgent.contains("edg")) return "Edge";
        if (userAgent.contains("chrome")) return "Chrome";
        if (userAgent.contains("firefox")) return "Firefox";
        if (userAgent.contains("safari") && !userAgent.contains("chrome")) return "Safari";
        if (userAgent.contains("opera") || userAgent.contains("opr")) return "Opera";

        return "Other";
    }



  

}
