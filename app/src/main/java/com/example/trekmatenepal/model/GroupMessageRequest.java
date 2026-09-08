package com.example.trekmatenepal.model;

public class GroupMessageRequest {
    private final String message_text;
    private final String message_type;
    public GroupMessageRequest(String text, String type) { this.message_text = text; this.message_type = type; }
}
