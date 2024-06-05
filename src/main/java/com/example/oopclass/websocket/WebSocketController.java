package com.example.oopclass.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);



    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public NotificationMessage sendMessage(NotificationMessage message) {
        logger.info("Received message: {}", message.getMessage());
        return message;
    }
}

