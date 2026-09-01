package com.example.trekmatenepal.model;

public class LoginRequest {

    private String email;
    private String password;
    private String role;

    public LoginRequest(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
