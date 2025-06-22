package com.lavanya.smartdelivery.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lavanya.smartdelivery.dto.OrderDTO;
import com.lavanya.smartdelivery.model.Customer;
import com.lavanya.smartdelivery.model.Order;
import com.lavanya.smartdelivery.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerService customerService;

    @Transactional
    public Order createOrder(OrderDTO orderDTO, OAuth2User principal) {
        Customer customer = customerService.getCustomerByEmail(principal.getAttribute("email"));
        
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderStatus("pending");
        order.setDeliveryAddress(orderDTO.getDeliveryAddress());
        order.setTotalAmount(orderDTO.getTotalAmount()); //dont use getter and setter , find another way 
        
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> getCustomerOrders(OAuth2User principal) {
        Customer customer = customerService.getCustomerByEmail(principal.getAttribute("email"));
        return orderRepository.findByCustomer_CustomerId(customer.getCustomerId());
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = getOrder(orderId);
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }
} 