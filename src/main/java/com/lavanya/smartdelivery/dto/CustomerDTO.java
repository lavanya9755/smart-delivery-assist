package com.lavanya.smartdelivery.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long customerId;
    private String name;
    private String address;
    private String phone;
    private String email;
} 