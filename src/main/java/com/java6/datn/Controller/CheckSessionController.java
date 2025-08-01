package com.java6.datn.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CheckSessionController {

    @GetMapping("/check-session")
    @ResponseBody
    public String checkSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            // User is authenticated
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Now you can access user information from userDetails
            String username = userDetails.getUsername();

            // Assuming your custom UserDetails has a method to get the user ID
            // Integer userId = ((CustomUserDetails) userDetails).getUserId(); // Replace CustomUserDetails

            return "User is logged in. Username: " + username /*+ ", User ID: " + userId*/;
        } else {
            return "User is not logged in.";
        }
    }
}
