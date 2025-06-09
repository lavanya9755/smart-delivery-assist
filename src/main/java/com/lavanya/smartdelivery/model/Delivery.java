package com.lavanya.smartdelivery.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deliveries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = true)
    private User driver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING;

    @Column(nullable = true)
    private LocalDateTime pickupTime;

    @Column(nullable = true)
    private LocalDateTime deliveryTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime bookingTime = LocalDateTime.now();

    @Column(nullable = true)
    private LocalDateTime estimatedDeliveryTime;

    @Column(nullable = true)
    private LocalDateTime actualDeliveryTime;

    @Column(nullable = false)
    private Boolean isFraudulent = false;

    @Column(nullable = true)
    private Double predictedEta;

    @OneToOne(mappedBy = "delivery", cascade = CascadeType.ALL)
    private Feedback feedback;

    public enum DeliveryStatus {
        PENDING,
        PICKED_UP,
        IN_TRANSIT,
        DELIVERED,
        CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        bookingTime = LocalDateTime.now();
    }
} 