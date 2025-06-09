package com.lavanya.smartdelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.lavanya.smartdelivery.service.CustomerService;

@Controller
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal OAuth2User principal) {
        // Create or update customer information when accessing dashboard
        if (principal != null) {
            customerService.createOrUpdateCustomer(principal);
        }
        return "dashboard";
    }
} 