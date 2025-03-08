package com.forestmonitoring.repository;

import com.forestmonitoring.model.Forest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForestRepository extends JpaRepository<Forest, Long> {
    Optional<Forest> findByForestToken(String forestToken);
}
