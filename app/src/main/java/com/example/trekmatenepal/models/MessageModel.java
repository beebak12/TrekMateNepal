package com.example.trekmatenepal.models;

public class MessageModel {

    private String senderName;
    private String messageText;
    private String messageTime;
    private boolean sentByCurrentUser;
    private int senderImage;

    public MessageModel(
            String senderName,
            String messageText,
            String messageTime,
            boolean sentByCurrentUser,
            int senderImage
    ) {
        this.senderName = senderName;
        this.messageText = messageText;
        this.messageTime = messageTime;
        this.sentByCurrentUser = sentByCurrentUser;
        this.senderImage = senderImage;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public boolean isSentByCurrentUser() {
        return sentByCurrentUser;
    }

    public int getSenderImage() {
        return senderImage;
    }
}