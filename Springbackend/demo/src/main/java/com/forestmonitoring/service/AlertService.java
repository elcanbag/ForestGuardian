package com.forestmonitoring.service;

import com.forestmonitoring.model.Alert;
import com.forestmonitoring.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public void processAlert(Alert alert) {
        alert.setTimestamp(LocalDateTime.now());
        alertRepository.save(alert);
    }
}
