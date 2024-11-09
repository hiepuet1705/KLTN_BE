package com.example.DA.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String senderId;
    private String recipientId; // ID of the post author
    private String content;
    private String timestamp;

    // Getters and Setters
}

