package com.lavanya.smartdelivery.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.lavanya.smartdelivery.model.Delivery.DeliveryStatus;

import lombok.Data;

@Data
public class DeliveryDTO {
    private Long deliveryId;
    private Long orderId;
    private Long driverId;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime pickupTime;
    private LocalDateTime deliveryTime;
    private String source;
    private String destination;
    private Double weight;
    private LocalDateTime bookingTime;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime actualDeliveryTime;
    private Boolean isFraudulent;
    private Double predictedEta;
    private BigDecimal price;
} 