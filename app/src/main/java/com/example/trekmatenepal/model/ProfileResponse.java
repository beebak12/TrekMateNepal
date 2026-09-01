package com.example.trekmatenepal.model;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private UserData data;

    @SerializedName("message")
    private String message;

    public boolean isSuccess() { return success; }
    public UserData getData() { return data; }
    public String getMessage() { return message; }

    public static class UserData {
        @SerializedName("id")
        private int id;

        @SerializedName("full_name")
        private String fullName;

        @SerializedName("username")
        private String username;

        @SerializedName("email")
        private String email;

        @SerializedName("phone")
        private String phone;

        @SerializedName("dob")
        private String dob;

        @SerializedName("gender")
        private String gender;

        @SerializedName("profile_image")
        private String profileImage;

        @SerializedName("bio")
        private String bio;

        @SerializedName("city")
        private String city;

        @SerializedName("country")
        private String country;

        public int getId() { return id; }
        public String getFullName() { return fullName; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getDob() { return dob; }
        public String getGender() { return gender; }
        public String getProfileImage() { return profileImage; }
        public String getBio() { return bio; }
        public String getCity() { return city; }
        public String getCountry() { return country; }
    }
}
