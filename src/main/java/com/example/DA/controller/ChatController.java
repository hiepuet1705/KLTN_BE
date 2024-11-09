package com.example.DA.controller;

import com.example.DA.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage") // Handle messages sent to /app/chat.sendMessage
    public void sendMessage(ChatMessage chatMessage) {
        // Send message to the specific user (post author)
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), // The recipient’s unique ID
                "/queue/messages", // Private queue for messages
                chatMessage
        );
    }
}

