package com.vinaycode.websocket.controller;

import com.vinaycode.websocket.model.WebsocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class WebsocketController {

    @MessageMapping("/message")
    @SendTo("topic/in")
    private String sendMessage(@RequestBody String message) {
        log.info("received : "+message);
        return message;
    }
}
