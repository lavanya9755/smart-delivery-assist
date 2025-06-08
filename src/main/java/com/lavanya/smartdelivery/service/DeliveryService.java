package com.lavanya.smartdelivery.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.lavanya.smartdelivery.dto.DeliveryDTO;
import com.lavanya.smartdelivery.model.Delivery;
import com.lavanya.smartdelivery.model.User;
import com.lavanya.smartdelivery.repository.DeliveryRepository;
import com.lavanya.smartdelivery.repository.UserRepository;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Delivery> getUserDeliveries(OAuth2User principal) {
        String email = principal.getAttribute("email");
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return deliveryRepository.findByUserOrderByBookingTimeDesc(user);
    }

    public Delivery createDelivery(OAuth2User principal, DeliveryDTO deliveryDTO) {
        String email = principal.getAttribute("email");
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Delivery delivery = new Delivery();
        delivery.setUser(user);
        delivery.setSource(deliveryDTO.getSource());
        delivery.setDestination(deliveryDTO.getDestination());
        delivery.setWeight(deliveryDTO.getWeight());
        delivery.setBookingTime(LocalDateTime.now());
        delivery.setDeliveryStatus(Delivery.DeliveryStatus.PENDING);

        return deliveryRepository.save(delivery);
    }
} 