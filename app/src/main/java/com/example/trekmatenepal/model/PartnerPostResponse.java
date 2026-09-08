package com.example.trekmatenepal.model;

public class PartnerPostResponse {
    private boolean success;
    private String message;
    private PartnerPostData data;
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public PartnerPostData getData() { return data; }
    public static class PartnerPostData {
        private int id;
        public int getId() { return id; }
    }
}
