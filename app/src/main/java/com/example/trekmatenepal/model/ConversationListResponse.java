package com.example.trekmatenepal.model;

import java.util.List;

public class ConversationListResponse {
    private boolean success;
    private List<ConversationData> data;
    public boolean isSuccess() { return success; }
    public List<ConversationData> getData() { return data; }
    public static class ConversationData {
        public int id;
        public int user_one_id;
        public int user_two_id;
        public String last_message_at;
        public int other_user_id;
        public String other_user_name;
    }
}
