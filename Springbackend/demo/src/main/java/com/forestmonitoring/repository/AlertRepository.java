package com.forestmonitoring.repository;

import com.forestmonitoring.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    
    List<Alert> findByForestForestToken(String forestToken);
}
