package com.example.trekmatenepal.model;

import java.util.List;

public class MessageListResponse {
    private boolean success;
    private List<MessageData> data;
    public boolean isSuccess() { return success; }
    public List<MessageData> getData() { return data; }
    public static class MessageData {
        public int id;
        public int sender_id;
        public int receiver_id;
        public String message_text;
        public String message_type;
        public String created_at;
    }
}
