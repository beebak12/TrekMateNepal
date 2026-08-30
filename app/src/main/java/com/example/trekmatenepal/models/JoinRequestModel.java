package com.example.trekmatenepal.models;

import java.io.Serializable;
import java.util.UUID;

public class JoinRequestModel implements Serializable {
    private String id;
    private String postId;
    private String requesterId;
    private String requesterName;
    private String status; // pending, accepted, rejected
    private long timestamp;

    public JoinRequestModel(String postId, String requesterId, String requesterName) {
        this.id = UUID.randomUUID().toString();
        this.postId = postId;
        this.requesterId = requesterId;
        this.requesterName = requesterName;
        this.status = "pending";
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public String getPostId() { return postId; }
    public String getRequesterId() { return requesterId; }
    public String getRequesterName() { return requesterName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getTimestamp() { return timestamp; }
}
