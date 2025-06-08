package com.lavanya.smartdelivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lavanya.smartdelivery.model.Delivery;
import com.lavanya.smartdelivery.model.Delivery.DeliveryStatus;
import com.lavanya.smartdelivery.model.Order;
import com.lavanya.smartdelivery.model.User;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByUserOrderByBookingTimeDesc(User user);
    
    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.isFraudulent = true")
    Long countFraudulentDeliveries();
    
    @Query("SELECT AVG(ABS(TIMESTAMPDIFF(MINUTE, d.estimatedDeliveryTime, d.actualDeliveryTime))) FROM Delivery d WHERE d.actualDeliveryTime IS NOT NULL")
    Double getAverageDeliveryTimeDifference();

    List<Delivery> findByDriver_UserId(Long driverId);
    List<Delivery> findByDeliveryStatus(DeliveryStatus status);
    List<Delivery> findByOrder(Order order);
} 