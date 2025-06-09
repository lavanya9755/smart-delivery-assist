package com.lavanya.smartdelivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lavanya.smartdelivery.model.User;
import com.lavanya.smartdelivery.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User getOrCreateUser(OAuth2User principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String picture = principal.getAttribute("picture");
        String googleId = principal.getAttribute("sub");

        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUsername(name);
                    newUser.setPicture(picture);
                    newUser.setGoogleId(googleId);
                    newUser.setPasswordHash("OAUTH2_USER"); // Placeholder for OAuth users
                    newUser.setRole(User.Role.ROLE_USER);
                    return userRepository.saveAndFlush(newUser);
                });
    }
} 