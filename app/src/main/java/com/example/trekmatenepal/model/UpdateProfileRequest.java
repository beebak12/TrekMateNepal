package com.example.trekmatenepal.model;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest {
    @SerializedName("full_name") private final String fullName;
    @SerializedName("username") private final String username;
    @SerializedName("email") private final String email;
    @SerializedName("phone") private final String phone;
    @SerializedName("dob") private final String dob;
    @SerializedName("gender") private final String gender;
    @SerializedName("bio") private final String bio;
    @SerializedName("city") private final String city;
    @SerializedName("country") private final String country;

    public UpdateProfileRequest(String fullName, String username, String email,
                                String phone, String dob, String gender,
                                String bio, String city, String country) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.gender = gender;
        this.bio = bio;
        this.city = city;
        this.country = country;
    }
}
