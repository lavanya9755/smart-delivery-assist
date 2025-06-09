package com.lavanya.smartdelivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lavanya.smartdelivery.model.Customer;
import com.lavanya.smartdelivery.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Customer createOrUpdateCustomer(OAuth2User principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        
        Customer customer = customerRepository.findByEmail(email)
            .orElse(new Customer());
        
        customer.setEmail(email);
        customer.setName(name);
        
        // If this is a new customer, set default values
        if (customer.getAddress() == null) {
            customer.setAddress("Not provided");
        }
        if (customer.getPhone() == null) {
            customer.setPhone("0000000000"); // 10 digits default phone
        }
        
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    }
} 