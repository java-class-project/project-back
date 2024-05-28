package com.example.oopclass.websocket;

import lombok.Data;

@Data
public class NotificationMessage {

    private String message;

    public NotificationMessage() {
    }

    public NotificationMessage(String message) {
        this.message = message;
    }
}
