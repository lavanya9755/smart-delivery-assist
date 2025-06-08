package com.lavanya.smartdelivery.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderDTO {
    private Long orderId;
    private Long customerId;
    private String orderStatus;
    private String deliveryAddress;
    private BigDecimal totalAmount;
} 