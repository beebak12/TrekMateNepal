package com.example.trekmatenepal.models;

import java.util.ArrayList;
import java.util.List;

public class ChatSummaryModel {
    private String id; // Use for groupId or userId
    private String name;
    private String lastMessage;
    private String time;
    private int imageRes;
    private String customImageUri;
    private int unreadCount;
    private boolean isGroup;
    private String adminId;
    private final ArrayList<String> memberIds = new ArrayList<>();

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
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public String getLastMessage() { return lastMessage; }
    public String getTime() { return time; }
    public int getImageRes() { return imageRes; }
    
    public String getCustomImageUri() { return customImageUri; }
    public void setCustomImageUri(String uri) { this.customImageUri = uri; }
    
    public int getUnreadCount() { return unreadCount; }
    public void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }
    
    public boolean isGroup() { return isGroup; }

    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }

    public List<String> getMemberIds() { return new ArrayList<>(memberIds); }
    public void setMemberIds(List<String> ids) {
        memberIds.clear();
        if (ids != null) memberIds.addAll(ids);
    }
    public void addMember(String userId) {
        if (userId == null || userId.trim().isEmpty()) return;
        for (String id : memberIds) if (id.equalsIgnoreCase(userId.trim())) return;
        memberIds.add(userId.trim());
    }
    public boolean hasMember(String userId) {
        if (userId == null) return false;
        for (String id : memberIds) if (id.equalsIgnoreCase(userId.trim())) return true;
        return false;
    }
}
