package com.example.trekmatenepal.model;

import java.util.List;

public class GroupMessageListResponse {
    private boolean success;
    private List<GroupMessageData> data;
    public boolean isSuccess() { return success; }
    public List<GroupMessageData> getData() { return data; }
    public static class GroupMessageData {
        public int id;
        public int sender_id;
        public String message_text;
        public String message_type;
        public String attachment_url;
        public String attachment_name;
        public String created_at;
    }
}
