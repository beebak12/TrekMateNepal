package com.example.trekmatenepal.models;

public class ChatSummaryModel {
    private String id; // Use for groupId or userId
    private String name;
    private String lastMessage;
    private String time;
    private int imageRes;
    private String customImageUri;
    private int unreadCount;
    private boolean isGroup;

    public ChatSummaryModel(String id, String name, String lastMessage, String time, int imageRes, int unreadCount, boolean isGroup) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.imageRes = imageRes;
        this.unreadCount = unreadCount;
        this.isGroup = isGroup;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getLastMessage() { return lastMessage; }
    public String getTime() { return time; }
    public int getImageRes() { return imageRes; }
    
    public String getCustomImageUri() { return customImageUri; }
    public void setCustomImageUri(String uri) { this.customImageUri = uri; }
    
    public int getUnreadCount() { return unreadCount; }
    public void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }
    
    public boolean isGroup() { return isGroup; }
}
