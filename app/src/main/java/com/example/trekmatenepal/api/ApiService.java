package com.example.trekmatenepal.api;

import com.example.trekmatenepal.model.LoginRequest;
import com.example.trekmatenepal.model.LoginResponse;
import com.example.trekmatenepal.model.ProfileResponse;
import com.example.trekmatenepal.model.RegisterRequest;
import com.example.trekmatenepal.model.UpdateProfileRequest;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

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
    Call<LoginResponse> loginUser(
            @Body LoginRequest request
    );

    // Get current user profile
    @GET("api/users/profile")
    Call<ProfileResponse> getProfile(
            @Header("Authorization") String token
    );

    // Update user profile
    @PUT("api/users/profile")
    Call<ProfileResponse> updateProfile(
            @Header("Authorization") String token,
            @Body UpdateProfileRequest request
    );

    @Multipart
    @POST("api/users/profile-image")
    Call<ProfileResponse> uploadProfileImage(
            @Header("Authorization") String token,
            @Part MultipartBody.Part image
    );

    @GET
    Call<ResponseBody> downloadImage(@Url String url);
}
