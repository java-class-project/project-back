package com.example.oopclass.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyUser(String user, String message) {
        NotificationMessage notificationMessage = new NotificationMessage(message);
        messagingTemplate.convertAndSendToUser(user, "/topic/notifications", notificationMessage);
    }
}
