package com.lavanya.smartdelivery.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lavanya.smartdelivery.dto.DeliveryDTO;
import com.lavanya.smartdelivery.model.Delivery;
import com.lavanya.smartdelivery.model.User;
import com.lavanya.smartdelivery.repository.DeliveryRepository;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public List<Delivery> getUserDeliveries(OAuth2User principal) {
        User user = userService.getOrCreateUser(principal);
        return deliveryRepository.findByUserOrderByBookingTimeDesc(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Delivery createDelivery(OAuth2User principal, DeliveryDTO deliveryDTO) {
        try {
            // Debug log incoming data
            System.out.println("\nStarting delivery creation in service layer"); // DEBUG , same getter setters!!
            System.out.println("Source: " + deliveryDTO.getSource());
            System.out.println("Destination: " + deliveryDTO.getDestination());
            System.out.println("Weight: " + deliveryDTO.getWeight());
            System.out.println("Price: " + deliveryDTO.getPrice());
            
            // Get or create user in a separate transaction
            User user = userService.getOrCreateUser(principal);
            System.out.println("User found/created - ID: " + user.getId());
            
            // Create and save the delivery
            Delivery delivery = new Delivery();
            delivery.setUser(user);
            delivery.setSource(deliveryDTO.getSource());
            delivery.setDestination(deliveryDTO.getDestination());
            delivery.setWeight(deliveryDTO.getWeight());
            
            // Handle price calculation if not set
            if (deliveryDTO.getPrice() == null || deliveryDTO.getPrice().compareTo(BigDecimal.ZERO) == 0) {
                double weight = deliveryDTO.getWeight();
                double basePrice = 100.0;
                double totalPrice = basePrice;
                
                if (weight > 1.0) {
                    totalPrice += Math.ceil(weight - 1.0) * 100.0;
                }
                
                delivery.setPrice(BigDecimal.valueOf(totalPrice));
                System.out.println("Calculated price: " + totalPrice);
            } else {
                delivery.setPrice(deliveryDTO.getPrice());
                System.out.println("Using provided price: " + deliveryDTO.getPrice());
            }
            
            // Set current time as booking time
            LocalDateTime now = LocalDateTime.now();
            delivery.setBookingTime(now);
            
            // Set estimated delivery time (30 minutes from now)
            delivery.setEstimatedDeliveryTime(now.plusMinutes(30));
            
            delivery.setDeliveryStatus(Delivery.DeliveryStatus.PENDING);
            
            // Set driver to the same user temporarily (this makes driver_id non-null)
            delivery.setDriver(user);
            delivery.setOrder(null);
            delivery.setIsFraudulent(false);

            // Debug log before saving
            System.out.println("\nAbout to save delivery with details:");
            System.out.println("User ID: " + delivery.getUser().getId());
            System.out.println("Driver ID: " + delivery.getDriver().getId());
            System.out.println("Source: " + delivery.getSource());
            System.out.println("Destination: " + delivery.getDestination());
            System.out.println("Weight: " + delivery.getWeight());
            System.out.println("Price: " + delivery.getPrice());
            System.out.println("Status: " + delivery.getDeliveryStatus());

            // Save and flush to ensure immediate persistence
            Delivery savedDelivery = deliveryRepository.saveAndFlush(delivery);
            
            // Log successful delivery creation
            System.out.println("\nSuccessfully created delivery with ID: " + savedDelivery.getDeliveryId());
            
            return savedDelivery;
        } catch (Exception e) {
            System.err.println("\nError creating delivery in service layer:");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error cause: " + (e.getCause() != null ? e.getCause().getMessage() : "No cause"));
            e.printStackTrace();
            throw new RuntimeException("Failed to create delivery: " + e.getMessage(), e);
        }
    }
} 