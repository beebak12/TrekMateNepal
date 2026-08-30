package com.example.trekmatenepal.models;

import java.io.Serializable;

public class JoinRequest implements Serializable {
    private String requestId;
    private String senderId;
    private String senderName;
    private String senderImage;
    private String receiverId;
    private String receiverName;
    private String trekName;
    private String message;
    private String status; // pending, accepted, rejected
    private long timestamp;
    private long createdDate;
    private String trekDate;

    public JoinRequest() {
    }

    public JoinRequest(String senderId, String senderName, String receiverId, 
                       String receiverName, String trekName, String message) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.trekName = trekName;
        this.message = message;
        this.status = "pending";
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getTrekName() {
        return trekName;
    }

    public void setTrekName(String trekName) {
        this.trekName = trekName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getTrekDate() {
        return trekDate;
    }

    public void setTrekDate(String trekDate) {
        this.trekDate = trekDate;
    }

    @Override
    public String toString() {
        return "JoinRequest{" +
                "requestId='" + requestId + '\'' +
                ", status='" + status + '\'' +
                ", trekName='" + trekName + '\'' +
                '}';
    }
}
