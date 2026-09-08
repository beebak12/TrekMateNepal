package com.example.trekmatenepal.model;

public class MessageRequest {
    private final int conversation_id;
    private final int receiver_id;
    private final String message_text;
    private final String message_type;
    public MessageRequest(int conversationId, int receiverId, String text, String type) {
        this.conversation_id = conversationId;
        this.receiver_id = receiverId;
        this.message_text = text;
        this.message_type = type;
    }
}
