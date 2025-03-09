package com.forestmonitoring.service;

import com.forestmonitoring.model.Role;
import com.forestmonitoring.model.User;
import com.forestmonitoring.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user, String adminUsername) {
        Optional<User> admin = userRepository.findByUsername(adminUsername);
        if (admin.isPresent() && admin.get().getRole() == Role.ADMIN) {
            return userRepository.save(user);
        }
        throw new RuntimeException("Only admins can add users");
    }

    public List<User> getUsers(String adminUsername) {
        return userRepository.findByRoleNot(Role.ADMIN);
    }

    public Optional<User> getUser(Long id, String adminUsername) {
        Optional<User> admin = userRepository.findByUsername(adminUsername);
        if (admin.isPresent() && admin.get().getRole() == Role.ADMIN) {
            return userRepository.findById(id);
        }
        return Optional.empty();
    }
}
