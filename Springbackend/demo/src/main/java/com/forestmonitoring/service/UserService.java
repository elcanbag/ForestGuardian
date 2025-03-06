package com.forestmonitoring.service;

import com.forestmonitoring.model.Role;
import com.forestmonitoring.model.User;
import com.forestmonitoring.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String username, String password, Role role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        return userRepository.save(user);
    }
}
