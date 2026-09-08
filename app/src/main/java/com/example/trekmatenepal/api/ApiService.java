package com.example.trekmatenepal.api;

import com.example.trekmatenepal.model.LoginRequest;
import com.example.trekmatenepal.model.LoginResponse;
import com.example.trekmatenepal.model.ProfileResponse;
import com.example.trekmatenepal.model.RegisterRequest;
import com.example.trekmatenepal.model.UpdateProfileRequest;
import com.example.trekmatenepal.model.GearListResponse;
import com.example.trekmatenepal.model.GearMutationResponse;
import com.example.trekmatenepal.model.GearRequest;
import com.example.trekmatenepal.model.FavoriteListResponse;
import com.example.trekmatenepal.model.FavoriteRequest;
import com.example.trekmatenepal.model.BasicApiResponse;
import com.example.trekmatenepal.model.PartnerPostRequest;
import com.example.trekmatenepal.model.PartnerPostResponse;
import com.example.trekmatenepal.model.PartnerPostListResponse;
import com.example.trekmatenepal.model.ConversationListResponse;
import com.example.trekmatenepal.model.ConversationRequest;
import com.example.trekmatenepal.model.ConversationResponse;
import com.example.trekmatenepal.model.MessageRequest;
import com.example.trekmatenepal.model.MessageListResponse;
import com.example.trekmatenepal.model.GroupRequest;
import com.example.trekmatenepal.model.GroupResponse;
import com.example.trekmatenepal.model.GroupListResponse;
import com.example.trekmatenepal.model.GroupMessageRequest;
import com.example.trekmatenepal.model.GroupMessageListResponse;
import com.example.trekmatenepal.model.GroupMemberRequest;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;
import retrofit2.http.Path;
import retrofit2.http.DELETE;

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

    @GET("api/gears") Call<GearListResponse> getGears();
    @GET("api/gears/mine") Call<GearListResponse> getMyGears(@Header("Authorization") String token);
    @POST("api/gears") Call<GearMutationResponse> createGear(
            @Header("Authorization") String token, @Body GearRequest request);
    @PUT("api/gears/{id}") Call<GearMutationResponse> updateGear(
            @Header("Authorization") String token, @Path("id") String id, @Body GearRequest request);
    @DELETE("api/gears/{id}") Call<GearMutationResponse> deleteGear(
            @Header("Authorization") String token, @Path("id") String id);
    @Multipart @POST("api/gears/{id}/image") Call<GearMutationResponse> uploadGearImage(
            @Header("Authorization") String token, @Path("id") String id,
            @Part MultipartBody.Part image);

    @GET("api/favorites") Call<FavoriteListResponse> getFavorites(@Header("Authorization") String token);
    @POST("api/favorites") Call<BasicApiResponse> addFavorite(@Header("Authorization") String token, @Body FavoriteRequest request);
    @DELETE("api/favorites/{type}/{id}") Call<BasicApiResponse> removeFavorite(@Header("Authorization") String token, @Path("type") String type, @Path("id") int id);

    @GET("api/partners") Call<PartnerPostListResponse> getPartnerPosts();
    @POST("api/partners") Call<PartnerPostResponse> createPartnerPost(@Header("Authorization") String token, @Body PartnerPostRequest request);
    @POST("api/partners/{id}/request") Call<BasicApiResponse> requestPartner(@Header("Authorization") String token, @Path("id") int id, @Body Object request);

    @GET("api/chat/conversations") Call<ConversationListResponse> getConversations(@Header("Authorization") String token);
    @POST("api/chat/conversations") Call<ConversationResponse> createConversation(@Header("Authorization") String token, @Body ConversationRequest request);
    @GET("api/chat/conversations/{id}/messages") Call<MessageListResponse> getMessages(@Header("Authorization") String token, @Path("id") int id);
    @POST("api/chat/messages") Call<BasicApiResponse> sendMessage(@Header("Authorization") String token, @Body MessageRequest request);

    @GET("api/chat/groups") Call<GroupListResponse> getGroups(@Header("Authorization") String token);
    @POST("api/chat/groups") Call<GroupResponse> createGroup(@Header("Authorization") String token, @Body GroupRequest request);
    @GET("api/chat/groups/{id}/messages") Call<GroupMessageListResponse> getGroupMessages(@Header("Authorization") String token, @Path("id") int id);
    @POST("api/chat/groups/{id}/messages") Call<BasicApiResponse> sendGroupMessage(@Header("Authorization") String token, @Path("id") int id, @Body GroupMessageRequest request);
    @Multipart @POST("api/chat/groups/{id}/attachments") Call<BasicApiResponse> uploadGroupAttachment(@Header("Authorization") String token, @Path("id") int id, @Part MultipartBody.Part attachment, @Part("message_text") RequestBody messageText);
    @POST("api/chat/groups/{id}/members") Call<BasicApiResponse> addGroupMember(@Header("Authorization") String token, @Path("id") int id, @Body GroupMemberRequest request);
}
