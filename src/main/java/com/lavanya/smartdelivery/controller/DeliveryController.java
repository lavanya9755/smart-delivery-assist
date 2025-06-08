package com.lavanya.smartdelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lavanya.smartdelivery.dto.DeliveryDTO;
import com.lavanya.smartdelivery.service.DeliveryService;

@Controller
@RequestMapping("/deliveries")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping
    public String listDeliveries(@AuthenticationPrincipal OAuth2User principal, Model model) {
        model.addAttribute("deliveries", deliveryService.getUserDeliveries(principal));
        return "delivery/list";
    }

    @GetMapping("/new")
    public String newDeliveryForm(Model model) {
        model.addAttribute("delivery", new DeliveryDTO());
        return "delivery/form";
    }

    @PostMapping("/new")
    public String createDelivery(@AuthenticationPrincipal OAuth2User principal, DeliveryDTO deliveryDTO) {
        deliveryService.createDelivery(principal, deliveryDTO);
        return "redirect:/deliveries";
    }
} 