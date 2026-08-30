package com.example.trekmatenepal.api;

import com.example.trekmatenepal.model.LoginRequest;
import com.example.trekmatenepal.model.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    // Test backend connection
    @GET("api/health")
    Call<Object> testConnection();

    // Register user
    @POST("api/auth/register")
    Call<Object> registerUser(
            @Body RegisterRequest request
    );

    // Login user
    @POST("api/auth/login")
    Call<Object> loginUser(
            @Body LoginRequest request
    );
}