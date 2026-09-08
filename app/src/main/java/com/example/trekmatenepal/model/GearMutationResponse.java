package com.example.trekmatenepal.model;

import com.google.gson.annotations.SerializedName;

public class GearMutationResponse {
    private boolean success;
    private String message;
    private Data data;
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Data getData() { return data; }
    public static class Data {
        private int id;
        @SerializedName("image_url") private String imageUrl;
        public int getId() { return id; }
        public String getImageUrl() { return imageUrl; }
    }
}
