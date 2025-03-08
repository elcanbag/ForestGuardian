package com.forestmonitoring.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forestmonitoring.model.Alert;
import com.forestmonitoring.repository.AlertRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final AlertRepository alertRepository;
    private final ObjectMapper objectMapper;

    public WebSocketHandler(AlertRepository alertRepository, ObjectMapper objectMapper) {
        this.alertRepository = alertRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Alert alert = objectMapper.readValue(message.getPayload(), Alert.class);
        alertRepository.save(alert);
    }
}
