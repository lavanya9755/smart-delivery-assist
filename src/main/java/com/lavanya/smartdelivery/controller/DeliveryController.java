package com.lavanya.smartdelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        DeliveryDTO delivery = new DeliveryDTO();
        model.addAttribute("delivery", delivery);
        return "delivery/form";
    }

    @PostMapping("/new")
    public String createDelivery(
            @AuthenticationPrincipal OAuth2User principal,
            @ModelAttribute("delivery") DeliveryDTO deliveryDTO,
            BindingResult bindingResult,
            Model model) {
        //DEBUG from here , check model and dto
        System.out.println("Received form submission in controller:");
        System.out.println("Principal: " + principal);
        System.out.println("Source: " + deliveryDTO.getSource());
        System.out.println("Destination: " + deliveryDTO.getDestination());
        System.out.println("Weight: " + deliveryDTO.getWeight());
        System.out.println("Price: " + deliveryDTO.getPrice());
        
        boolean hasErrors = false;
        if (deliveryDTO.getSource() == null || deliveryDTO.getSource().trim().isEmpty()) {
            bindingResult.rejectValue("source", "error.source", "Source location is required");
            hasErrors = true;
        }
        if (deliveryDTO.getDestination() == null || deliveryDTO.getDestination().trim().isEmpty()) {
            bindingResult.rejectValue("destination", "error.destination", "Destination location is required");
            hasErrors = true;
        }
        if (deliveryDTO.getWeight() == null || deliveryDTO.getWeight() <= 0) {
            bindingResult.rejectValue("weight", "error.weight", "Weight must be greater than 0");
            hasErrors = true;
        }
        
        // If there are validation errors, return to form
        if (hasErrors) {
            System.out.println("Validation errors found: " + bindingResult.getAllErrors());
            model.addAttribute("error", "Please fix the validation errors below.");
            return "delivery/form";
        }

        try {
            // Try to create the delivery
            deliveryService.createDelivery(principal, deliveryDTO);
            return "redirect:/deliveries";
        } catch (Exception e) {
            // Log the full error
            System.err.println("Error in controller while creating delivery:");
            e.printStackTrace();
            
            // Add detailed error message to the model
            String errorMessage = "Failed to create delivery: ";
            if (e.getMessage() != null) {
                errorMessage += e.getMessage();
            } else if (e.getCause() != null && e.getCause().getMessage() != null) {
                errorMessage += e.getCause().getMessage();
            } else {
                errorMessage += "An unexpected error occurred. Please try again.";
            }
            
            model.addAttribute("error", errorMessage);
            return "delivery/form";
        }
    }
} 