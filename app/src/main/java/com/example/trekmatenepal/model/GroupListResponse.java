package com.example.trekmatenepal.model;

import java.util.List;

public class GroupListResponse {
    private boolean success;
    private List<GroupData> data;
    public boolean isSuccess() { return success; }
    public List<GroupData> getData() { return data; }
    public static class GroupData {
        public int id;
        public String name;
        public int owner_id;
        public int member_count;
    }
}
