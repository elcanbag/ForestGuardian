package com.forestmonitoring.service;

import com.forestmonitoring.model.Role;
import com.forestmonitoring.model.User;
import com.forestmonitoring.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new AccessDeniedException("Unauthorized"));

        if (admin.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can add users");
        }

        return userRepository.save(user);
    }

    public List<User> getUsers(String adminUsername) {
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new AccessDeniedException("Unauthorized"));

        if (admin.getRole() == Role.ADMIN) {
            return userRepository.findAll();
        } else {
            throw new AccessDeniedException("Only admins can view all users");
        }
    }

    public Optional<User> getUser(Long id, String username) {
        User admin = userRepository.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("Unauthorized"));

        if (admin.getRole() == Role.ADMIN) {
            return userRepository.findById(id);
        } else {
            return userRepository.findById(id)
                    .filter(user -> user.getUsername().equals(username));
        }
    }

    public void deleteUser(Long id, String adminUsername) {
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new AccessDeniedException("Unauthorized"));

        if (admin.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can delete users");
        }

        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userToDelete.getRole() == Role.ADMIN) {
            throw new AccessDeniedException("Admins cannot delete other admins");
        }

        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User updatedUser, String requesterUsername) {
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new AccessDeniedException("Unauthorized"));

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (requester.getRole() != Role.ADMIN && !existingUser.getUsername().equals(requesterUsername)) {
            throw new AccessDeniedException("You can only update your own details");
        }

        if (updatedUser.getUsername() != null && requester.getRole() == Role.ADMIN) {
            existingUser.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(updatedUser.getPassword());
        }

        if (updatedUser.getRole() != null && requester.getRole() == Role.ADMIN) {
            existingUser.setRole(updatedUser.getRole());
        }

        return userRepository.save(existingUser);
    }
}
