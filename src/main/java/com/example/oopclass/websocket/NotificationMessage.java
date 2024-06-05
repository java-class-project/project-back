package com.example.oopclass.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class NotificationMessage {

    private String type;

    private String message;

}




