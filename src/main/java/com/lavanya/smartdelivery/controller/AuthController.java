package com.lavanya.smartdelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.lavanya.smartdelivery.service.CustomerService;
import com.lavanya.smartdelivery.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, 
                          @AuthenticationPrincipal OAuth2User oauth2User,
                          Model model) {
        
        if (userDetails != null) {
            // Traditional login user
            model.addAttribute("user", userDetails);
            model.addAttribute("isAdmin", userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        } else if (oauth2User != null) {
            // OAuth2 user
            model.addAttribute("user", oauth2User);
            model.addAttribute("isAdmin", false); // OAuth2 users are regular users by default
            customerService.createOrUpdateCustomer(oauth2User);
        }
        
        return "dashboard";
    }

    @GetMapping("/admin")
    public String adminDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("user", userDetails);
            return "admin/dashboard";
        }
        return "redirect:/dashboard";
    }
} 