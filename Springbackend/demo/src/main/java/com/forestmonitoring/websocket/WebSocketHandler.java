package com.forestmonitoring.websocket;

import com.forestmonitoring.model.Alert;
import com.forestmonitoring.service.AlertService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final AlertService alertService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WebSocketHandler(AlertService alertService) {
        this.alertService = alertService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        Alert alert = objectMapper.readValue(payload, Alert.class);
        alertService.processAlert(alert);
    }
}
