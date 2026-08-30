package com.example.trekmatenepal.models;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private String messageId;
    private String senderId;
    private String senderName;
    private String senderImage;
    private String receiverId;
    private String message;
    private long timestamp;
    private boolean isRead;
    private String chatRoomId;

    public ChatMessage() {
    }

    public ChatMessage(String senderId, String senderName, String receiverId, String message) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        this.isRead = false;
    }

    // Getters and Setters
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "messageId='" + messageId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
