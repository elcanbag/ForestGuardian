package com.forestmonitoring.repository;

import com.forestmonitoring.model.User;
import com.forestmonitoring.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRoleNot(Role role);
    List<User> findByUsernameContainingIgnoreCase(String username);

}
