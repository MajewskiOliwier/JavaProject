package com.example.JavaProject.controller;

import com.example.JavaProject.service.interfaces.NotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public String sendNotificationWebSocket(String message) {
        return message;
    }


    @PostMapping("/send")
    public String sendNotification(@RequestBody String message) {
        notificationService.sendNotification(message);
        return "Notification sent successfully!";
    }
}
