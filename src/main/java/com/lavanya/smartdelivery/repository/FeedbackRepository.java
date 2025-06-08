package com.lavanya.smartdelivery.repository;

import com.lavanya.smartdelivery.model.Feedback;
import com.lavanya.smartdelivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByUserOrderByCreatedAtDesc(User user);
    
    @Query("SELECT AVG(f.sentimentScore) FROM Feedback f")
    Double getAverageSentimentScore();
    
    @Query("SELECT f.rating, COUNT(f) FROM Feedback f GROUP BY f.rating")
    List<Object[]> getRatingDistribution();
} 