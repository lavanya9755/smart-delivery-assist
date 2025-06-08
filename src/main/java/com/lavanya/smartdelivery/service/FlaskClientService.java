package com.lavanya.smartdelivery.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class FlaskClientService {
    private final RestTemplate restTemplate;
    private final String flaskBaseUrl = "http://localhost:5000";

    public FlaskClientService() {
        this.restTemplate = new RestTemplate();
    }

    public Double predictEta(Map<String, Object> data) {
        String url = flaskBaseUrl + "/predict_eta";
        return makePostRequest(url, data, Double.class);
    }

    public Boolean detectFraud(Map<String, Object> data) {
        String url = flaskBaseUrl + "/detect_fraud";
        return makePostRequest(url, data, Boolean.class);
    }

    public String analyzeSentiment(String text) {
        String url = flaskBaseUrl + "/analyze_sentiment";
        return makePostRequest(url, Map.of("text", text), String.class);
    }

    public Integer clusterUser(Map<String, Object> data) {
        String url = flaskBaseUrl + "/cluster_user";
        return makePostRequest(url, data, Integer.class);
    }

    private <T> T makePostRequest(String url, Object body, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url, request, responseType);
    }
} 