package com.example.trekmatenepal.models;

public class ChatListModel {

    private final String chatName;
    private final String chatSubtitle;
    private final String lastMessage;
    private final String messageTime;
    private final int chatImage;
    private final int unreadCount;

    public ChatListModel(
            String chatName,
            String chatSubtitle,
            String lastMessage,
            String messageTime,
            int chatImage,
            int unreadCount
    ) {
        this.chatName = chatName;
        this.chatSubtitle = chatSubtitle;
        this.lastMessage = lastMessage;
        this.messageTime = messageTime;
        this.chatImage = chatImage;
        this.unreadCount = unreadCount;
    }

    public String getChatName() {
        return chatName;
    }

    public String getChatSubtitle() {
        return chatSubtitle;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public int getChatImage() {
        return chatImage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }
}