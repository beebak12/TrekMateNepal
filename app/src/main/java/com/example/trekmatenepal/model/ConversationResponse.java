package com.example.trekmatenepal.model;

public class ConversationResponse {
    private boolean success;
    private ConversationListResponse.ConversationData data;
    public boolean isSuccess() { return success; }
    public ConversationListResponse.ConversationData getData() { return data; }
}
