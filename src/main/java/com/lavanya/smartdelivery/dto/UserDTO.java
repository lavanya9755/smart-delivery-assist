package com.lavanya.smartdelivery.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private String password; // Only for input, never for output
} 