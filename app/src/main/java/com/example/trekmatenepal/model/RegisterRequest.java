package com.example.trekmatenepal.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("full_name")
    private String full_name;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("phone")
    private String phone;

    @SerializedName("dob")
    private String dob;

    @SerializedName("gender")
    private String gender;

    @SerializedName("address")
    private String address;

    public RegisterRequest(
            String full_name,
            String username,
            String email,
            String password,
            String phone,
            String dob,
            String gender,
            String address) {

        this.full_name = full_name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

}
