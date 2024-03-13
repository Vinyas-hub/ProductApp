package org.acme.auth;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.entity.User;
import org.acme.repository.UserRepository;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.io.IOException;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository userRepository;

    @Inject
    JsonWebToken jwt;

    @Transactional
    public User registerUser(User user) {
        // Check if username or email already exists
        if (userRepository.find("username", user.getUsername()).count() > 0 ||
                userRepository.find("email", user.getEmail()).count() > 0) {
            return null; // User already exists
        }
        userRepository.persist(user);
        return user;
    }

    public String loginUser(User user) {
        User existingUser = userRepository.find("username", user.getUsername()).firstResult();
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            try {
                return JwtUtils.generateJwtToken(existingUser.getUsername());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null; // Authentication failed
    }

    private String generateToken(String username) {
        // Generate JWT token here
        // You can use libraries like SmallRye JWT or others to generate JWT tokens
        // This is just a placeholder, you need to implement it according to your setup
        return ""; // Return JWT token
    }
}