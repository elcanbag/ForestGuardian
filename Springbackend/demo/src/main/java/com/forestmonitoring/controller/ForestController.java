package com.forestmonitoring.controller;

import com.forestmonitoring.model.Alert;
import com.forestmonitoring.model.Forest;
import com.forestmonitoring.repository.AlertRepository;
import com.forestmonitoring.repository.ForestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/forests")
public class ForestController {

    private final ForestRepository forestRepository;
    private final AlertRepository alertRepository;

    public ForestController(ForestRepository forestRepository, AlertRepository alertRepository) {
        this.forestRepository = forestRepository;
        this.alertRepository = alertRepository;
    }

    @PostMapping
    public ResponseEntity<Forest> createForest(@RequestBody Forest forest) {
        Forest createdForest = forestRepository.save(forest);
        return ResponseEntity.ok(createdForest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Forest> updateForest(@PathVariable Long id, @RequestBody Forest forest) {
        return forestRepository.findById(id).map(existingForest -> {
            existingForest.setName(forest.getName());
            existingForest.setForestToken(forest.getForestToken());
            Forest updatedForest = forestRepository.save(existingForest);
            return ResponseEntity.ok(updatedForest);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForest(@PathVariable Long id) {
        if (forestRepository.existsById(id)) {
            forestRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{forestToken}")
    public ResponseEntity<Map<String, Object>> getForest(@PathVariable String forestToken) {
        return forestRepository.findByForestToken(forestToken).map(forest -> {
            Map<String, Object> response = new HashMap<>();
            response.put("id", forest.getId());
            response.put("name", forest.getName());
            response.put("forestToken", forest.getForestToken());

            List<Alert> alerts = alertRepository.findByForestToken(forestToken);
            if (!alerts.isEmpty()) {
                Alert lastAlert = alerts.get(alerts.size() - 1);
                Map<String, Object> alertData = new HashMap<>();
                alertData.put("alertType", lastAlert.getAlertType());
                alertData.put("timestamp", lastAlert.getTimestamp());
                response.put("latestAlert", alertData);
            } else {
                response.put("latestAlert", null);
            }

            return ResponseEntity.ok(response);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchForests(@RequestParam String name) {
        List<Forest> forests = forestRepository.findByNameContainingIgnoreCase(name);

        List<Map<String, Object>> responseList = forests.stream().map(forest -> {
            Map<String, Object> response = new HashMap<>();
            response.put("id", forest.getId());
            response.put("name", forest.getName());
            response.put("forestToken", forest.getForestToken());

            List<Alert> alerts = alertRepository.findByForestToken(forest.getForestToken());
            if (!alerts.isEmpty()) {
                Alert lastAlert = alerts.get(alerts.size() - 1);
                Map<String, Object> alertData = new HashMap<>();
                alertData.put("alertType", lastAlert.getAlertType());
                alertData.put("timestamp", lastAlert.getTimestamp());
                response.put("latestAlert", alertData);
            } else {
                response.put("latestAlert", null);
            }

            return response;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllForestsWithLatestAlerts() {
        List<Forest> forests = forestRepository.findAll();

        List<Map<String, Object>> responseList = forests.stream().map(forest -> {
            Map<String, Object> response = new HashMap<>();
            response.put("id", forest.getId());
            response.put("name", forest.getName());
            response.put("forestToken", forest.getForestToken());

            List<Alert> alerts = alertRepository.findByForestToken(forest.getForestToken());
            if (!alerts.isEmpty()) {
                Alert lastAlert = alerts.get(alerts.size() - 1);
                Map<String, Object> alertData = new HashMap<>();
                alertData.put("alertType", lastAlert.getAlertType());
                alertData.put("timestamp", lastAlert.getTimestamp());
                response.put("latestAlert", alertData);
            } else {
                response.put("latestAlert", null);
            }

            return response;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }
}
