package com.vinaycode.websocket.controller;

import com.vinaycode.websocket.model.WebsocketMessage;
import com.vinaycode.websocket.service.WebsocketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
@Slf4j
@AllArgsConstructor
public class WebsocketController {

    private final WebsocketService websocketService;

    @MessageMapping("/message")
    @SendTo("topic/in")
    private String sendMessage(@RequestBody String message) {
        log.info("Received : "+message);
        return message;
    }

    @MessageMapping("/messages.{user}")
    private Boolean sendMessageToUser(Principal principal, @Header String authKey, @DestinationVariable String user, @RequestBody WebsocketMessage message) {
        log.info("Send message from user {} to user {}. Auth key {}", principal.getName(), user, authKey);
        websocketService.notifyUser(user,message.getMessage());
        return Boolean.TRUE;
    }
}
