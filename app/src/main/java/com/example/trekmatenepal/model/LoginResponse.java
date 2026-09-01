package com.example.trekmatenepal.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private User user;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getToken() { return token; }
    public User getUser() { return user; }

    public static class User {
        @SerializedName("id")
        private int id;

        @SerializedName("full_name")
        private String fullName;

        @SerializedName("username")
        private String username;

        @SerializedName("email")
        private String email;

        @SerializedName("role_id")
        private int roleId;

        @SerializedName("role")
        private String role;

        public int getId() { return id; }
        public String getFullName() { return fullName; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public int getRoleId() { return roleId; }
        public String getRole() { return role; }
    }
}
