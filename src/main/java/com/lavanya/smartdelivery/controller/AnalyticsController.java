package com.lavanya.smartdelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.lavanya.smartdelivery.service.AnalyticsService;

@Controller
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/analytics")
    public String showAnalytics(@AuthenticationPrincipal OAuth2User principal, Model model) {
        // Add analytics data to the model
        model.addAttribute("totalDeliveries", analyticsService.getTotalDeliveries());
        model.addAttribute("avgDeliveryTime", analyticsService.getAverageDeliveryTime());
        model.addAttribute("fraudPercentage", analyticsService.getFraudPercentage());
        model.addAttribute("avgSentimentScore", analyticsService.getAverageSentimentScore());
        model.addAttribute("deliveryStatusDistribution", analyticsService.getDeliveryStatusDistribution());
        model.addAttribute("ratingDistribution", analyticsService.getRatingDistribution());
        
        return "analytics/dashboard";
    }
} 