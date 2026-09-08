package com.example.trekmatenepal.model;

public class GroupResponse {
    private boolean success;
    private String message;
    private GroupData data;
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public GroupData getData() { return data; }
    public static class GroupData { public int id; public String name; public int owner_id; }
}
