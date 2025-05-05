package com.example.pokevault.service;

import com.example.pokevault.model.User;
import com.example.pokevault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public boolean registerNewUser(User user) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(user.getUsername()) || 
            userRepository.existsByEmail(user.getEmail())) {
            return false;
        }
        
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Save user
        userRepository.save(user);
        return true;
    }
}