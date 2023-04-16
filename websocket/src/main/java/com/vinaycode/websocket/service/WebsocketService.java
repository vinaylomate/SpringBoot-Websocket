package com.vinaycode.websocket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class WebsocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyUser(final String userId, final String message) {
        this.send(userId, message);
    }

    @SneakyThrows
    private void  send(String userId, String message) {
        String json = (new ObjectMapper()).writeValueAsString(new WebSocketResponseMessage(message));
        messagingTemplate.convertAndSendToUser(userId, "/topic/messages", json);
        log.info("After Messaging Template = "+userId);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WebSocketResponseMessage {
        private String content;
    }
}
