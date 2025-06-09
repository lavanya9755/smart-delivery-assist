package com.lavanya.smartdelivery.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lavanya.smartdelivery.model.Delivery;
import com.lavanya.smartdelivery.repository.DeliveryRepository;
import com.lavanya.smartdelivery.repository.FeedbackRepository;

@Service
public class AnalyticsService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Transactional(readOnly = true)
    public long getTotalDeliveries() {
        return deliveryRepository.count();
    }

    @Transactional(readOnly = true)
    public double getAverageDeliveryTime() {
        Double avgTime = deliveryRepository.getAverageDeliveryTimeDifference();
        return avgTime != null ? avgTime : 0.0;
    }

    @Transactional(readOnly = true)
    public double getFraudPercentage() {
        long totalDeliveries = deliveryRepository.count();
        if (totalDeliveries == 0) return 0.0;
        
        long fraudulentDeliveries = deliveryRepository.countFraudulentDeliveries();
        return (fraudulentDeliveries * 100.0) / totalDeliveries;
    }

    @Transactional(readOnly = true)
    public double getAverageSentimentScore() {
        Double avgScore = feedbackRepository.getAverageSentimentScore();
        return avgScore != null ? avgScore : 0.0;
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getDeliveryStatusDistribution() {
        Map<String, Long> distribution = new HashMap<>();
        for (Delivery.DeliveryStatus status : Delivery.DeliveryStatus.values()) {
            long count = deliveryRepository.countByDeliveryStatus(status);
            distribution.put(status.name(), count);
        }
        return distribution;
    }

    @Transactional(readOnly = true)
    public Map<Integer, Long> getRatingDistribution() {
        Map<Integer, Long> distribution = new HashMap<>();
        List<Object[]> results = feedbackRepository.getRatingDistribution();
        
        for (Object[] result : results) {
            Integer rating = (Integer) result[0];
            Long count = (Long) result[1];
            distribution.put(rating, count);
        }
        
        return distribution;
    }
} 