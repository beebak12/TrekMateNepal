package com.example.trekmatenepal.data;

import android.content.Context;
import com.example.trekmatenepal.R;
import com.example.trekmatenepal.api.ApiClient;
import com.example.trekmatenepal.api.ApiService;
import com.example.trekmatenepal.model.ConversationListResponse;
import com.example.trekmatenepal.model.ConversationRequest;
import com.example.trekmatenepal.model.ConversationResponse;
import com.example.trekmatenepal.model.MessageListResponse;
import com.example.trekmatenepal.model.MessageRequest;
import com.example.trekmatenepal.model.GroupRequest;
import com.example.trekmatenepal.model.GroupResponse;
import com.example.trekmatenepal.model.GroupListResponse;
import com.example.trekmatenepal.model.GroupMessageRequest;
import com.example.trekmatenepal.model.GroupMessageListResponse;
import com.example.trekmatenepal.model.GroupMemberRequest;
import com.example.trekmatenepal.models.ChatMessageModel;
import com.example.trekmatenepal.models.ChatSummaryModel;
import java.util.ArrayList;
import java.util.List;
import android.net.Uri;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Authenticated direct-chat synchronization. Group chats remain local until a group API is available. */
public final class ChatBackendRepository {
    public interface ConversationCallback { void success(int conversationId); void error(String message); }
    public interface MessagesCallback { void success(ArrayList<ChatMessageModel> messages); void error(String message); }
    public interface GroupCallback { void success(int groupId); void error(String message); }
    private ChatBackendRepository() { }

    public static void syncConversations(Context context, Runnable complete) {
        String token = SessionUser.getToken(context);
        if (token == null || token.trim().isEmpty()) { if (complete != null) complete.run(); return; }
        ApiClient.getClient().create(ApiService.class).getConversations("Bearer " + token)
                .enqueue(new Callback<ConversationListResponse>() {
                    @Override public void onResponse(Call<ConversationListResponse> call, Response<ConversationListResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            ChatRepository.loadChats(context);
                            if (response.body().getData() != null) for (ConversationListResponse.ConversationData c : response.body().getData()) {
                                String name = c.other_user_name == null || c.other_user_name.trim().isEmpty() ? "Trek Partner" : c.other_user_name;
                                ChatSummaryModel chat = new ChatSummaryModel(String.valueOf(c.id), name, "", "", R.drawable.partner1, 0, false);
                                chat.setAdminId(String.valueOf(c.other_user_id));
                                ChatRepository.addChat(context, chat);
                            }
                        }
                        if (complete != null) complete.run();
                    }
                    @Override public void onFailure(Call<ConversationListResponse> call, Throwable t) { if (complete != null) complete.run(); }
                });
    }

    public static void openConversation(Context context, int partnerId, ConversationCallback callback) {
        String token = SessionUser.getToken(context);
        if (token == null || token.trim().isEmpty() || partnerId <= 0) { callback.error("Direct chat requires a signed-in server user"); return; }
        ApiClient.getClient().create(ApiService.class).createConversation("Bearer " + token, new ConversationRequest(partnerId))
                .enqueue(new Callback<ConversationResponse>() {
                    @Override public void onResponse(Call<ConversationResponse> call, Response<ConversationResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess() && response.body().getData() != null) callback.success(response.body().getData().id);
                        else callback.error("Unable to open conversation");
                    }
                    @Override public void onFailure(Call<ConversationResponse> call, Throwable t) { callback.error("Chat server unavailable"); }
                });
    }

    public static void loadMessages(Context context, int conversationId, MessagesCallback callback) {
        String token = SessionUser.getToken(context);
        if (token == null || token.trim().isEmpty() || conversationId <= 0) { callback.error("No server conversation"); return; }
        ApiClient.getClient().create(ApiService.class).getMessages("Bearer " + token, conversationId)
                .enqueue(new Callback<MessageListResponse>() {
                    @Override public void onResponse(Call<MessageListResponse> call, Response<MessageListResponse> response) {
                        if (!response.isSuccessful() || response.body() == null || !response.body().isSuccess()) { callback.error("Unable to load messages"); return; }
                        ArrayList<ChatMessageModel> result = new ArrayList<>();
                        String me = SessionUser.getUserId(context);
                        if (response.body().getData() != null) for (MessageListResponse.MessageData m : response.body().getData()) {
                            int type = String.valueOf(m.sender_id).equals(me) ? ChatMessageModel.TYPE_SENT : ChatMessageModel.TYPE_RECEIVED;
                            result.add(new ChatMessageModel(m.message_text, m.created_at == null ? "" : m.created_at, type));
                        }
                        callback.success(result);
                    }
                    @Override public void onFailure(Call<MessageListResponse> call, Throwable t) { callback.error("Chat server unavailable"); }
                });
    }

    public static void sendMessage(Context context, int conversationId, int receiverId, String text) {
        String token = SessionUser.getToken(context);
        if (token == null || token.trim().isEmpty() || conversationId <= 0 || receiverId <= 0 || text == null || text.trim().isEmpty()) return;
        ApiClient.getClient().create(ApiService.class).sendMessage("Bearer " + token,
                new MessageRequest(conversationId, receiverId, text.trim(), "text")).enqueue(new Callback<com.example.trekmatenepal.model.BasicApiResponse>() {
            @Override public void onResponse(Call<com.example.trekmatenepal.model.BasicApiResponse> call, Response<com.example.trekmatenepal.model.BasicApiResponse> response) { }
            @Override public void onFailure(Call<com.example.trekmatenepal.model.BasicApiResponse> call, Throwable t) { }
        });
    }

    public static void createGroup(Context context, String name, Integer partnerPostId, GroupCallback callback) {
        String token = SessionUser.getToken(context);
        if (token == null || token.trim().isEmpty()) { callback.error("Group sync requires a signed-in server user"); return; }
        ApiClient.getClient().create(ApiService.class).createGroup("Bearer " + token, new GroupRequest(name, partnerPostId))
                .enqueue(new Callback<GroupResponse>() {
                    @Override public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess() && response.body().getData() != null) callback.success(response.body().getData().id);
                        else callback.error("Unable to save group");
                    }
                    @Override public void onFailure(Call<GroupResponse> call, Throwable t) { callback.error("Group server unavailable"); }
                });
    }

    public static void syncGroups(Context context, Runnable complete) {
        String token = SessionUser.getToken(context);
        if (token == null || token.trim().isEmpty()) { if (complete != null) complete.run(); return; }
        ApiClient.getClient().create(ApiService.class).getGroups("Bearer " + token).enqueue(new Callback<GroupListResponse>() {
            @Override public void onResponse(Call<GroupListResponse> call, Response<GroupListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    ChatRepository.loadChats(context);
                    if (response.body().getData() != null) for (GroupListResponse.GroupData group : response.body().getData()) {
                        ChatSummaryModel chat = new ChatSummaryModel(String.valueOf(group.id), group.name, "", "", R.drawable.everest, 0, true);
                        chat.setAdminId(String.valueOf(group.owner_id)); chat.addMember(SessionUser.getUserId(context)); ChatRepository.addChat(context, chat);
                    }
                }
                if (complete != null) complete.run();
            }
            @Override public void onFailure(Call<GroupListResponse> call, Throwable t) { if (complete != null) complete.run(); }
        });
    }

    public static void loadGroupMessages(Context context, int groupId, MessagesCallback callback) {
        String token = SessionUser.getToken(context);
        if (token == null || token.trim().isEmpty() || groupId <= 0) { callback.error("No server group"); return; }
        ApiClient.getClient().create(ApiService.class).getGroupMessages("Bearer " + token, groupId).enqueue(new Callback<GroupMessageListResponse>() {
            @Override public void onResponse(Call<GroupMessageListResponse> call, Response<GroupMessageListResponse> response) {
                if (!response.isSuccessful() || response.body() == null || !response.body().isSuccess()) { callback.error("Unable to load group messages"); return; }
                ArrayList<ChatMessageModel> result = new ArrayList<>();
                String me = SessionUser.getUserId(context);
                if (response.body().getData() != null) for (GroupMessageListResponse.GroupMessageData message : response.body().getData()) {
                    int type = String.valueOf(message.sender_id).equals(me) ? ChatMessageModel.TYPE_SENT : ChatMessageModel.TYPE_RECEIVED;
                    ChatMessageModel item = new ChatMessageModel(message.message_text, message.created_at == null ? "" : message.created_at, type);
                    if (message.attachment_url != null && !message.attachment_url.isEmpty()) item.setRemoteAttachment(message.attachment_url, message.attachment_name, message.message_type);
                    result.add(item);
                }
                callback.success(result);
            }
            @Override public void onFailure(Call<GroupMessageListResponse> call, Throwable t) { callback.error("Group server unavailable"); }
        });
    }

    public static void sendGroupMessage(Context context, int groupId, String text) {
        String token = SessionUser.getToken(context);
        if (token == null || token.trim().isEmpty() || groupId <= 0 || text == null || text.trim().isEmpty()) return;
        ApiClient.getClient().create(ApiService.class).sendGroupMessage("Bearer " + token, groupId, new GroupMessageRequest(text, "text")).enqueue(new Callback<com.example.trekmatenepal.model.BasicApiResponse>() {
            @Override public void onResponse(Call<com.example.trekmatenepal.model.BasicApiResponse> call, Response<com.example.trekmatenepal.model.BasicApiResponse> response) { }
            @Override public void onFailure(Call<com.example.trekmatenepal.model.BasicApiResponse> call, Throwable t) { }
        });
    }

    public static void uploadGroupAttachment(Context context, int groupId, Uri uri, String text, GroupCallback callback) {
        String token = SessionUser.getToken(context);
        if (token == null || token.trim().isEmpty() || groupId <= 0 || uri == null) { callback.error("Attachment sync requires a signed-in server user"); return; }
        try {
            byte[] bytes;
            try (InputStream input = context.getContentResolver().openInputStream(uri); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[8192]; int count; while ((count = input.read(buffer)) >= 0) output.write(buffer, 0, count); bytes = output.toByteArray();
            }
            String mime = context.getContentResolver().getType(uri); if (mime == null) mime = "application/octet-stream";
            String name = uri.getLastPathSegment() == null ? "attachment" : uri.getLastPathSegment();
            MultipartBody.Part part = MultipartBody.Part.createFormData("attachment", name, RequestBody.create(MediaType.parse(mime), bytes));
            RequestBody message = RequestBody.create(MediaType.parse("text/plain"), text == null ? "" : text);
            ApiClient.getClient().create(ApiService.class).uploadGroupAttachment("Bearer " + token, groupId, part, message).enqueue(new Callback<com.example.trekmatenepal.model.BasicApiResponse>() {
                @Override public void onResponse(Call<com.example.trekmatenepal.model.BasicApiResponse> call, Response<com.example.trekmatenepal.model.BasicApiResponse> response) { if (response.isSuccessful()) callback.success(groupId); else callback.error("Unable to upload attachment (" + response.code() + ")"); }
                @Override public void onFailure(Call<com.example.trekmatenepal.model.BasicApiResponse> call, Throwable t) { callback.error("Attachment server unavailable"); }
            });
        } catch (Exception error) { callback.error("Unable to read attachment"); }
    }

    public static void addGroupMember(Context context, int groupId, int userId) {
        String token = SessionUser.getToken(context);
        if (token == null || token.trim().isEmpty() || groupId <= 0 || userId <= 0) return;
        ApiClient.getClient().create(ApiService.class).addGroupMember("Bearer " + token, groupId, new GroupMemberRequest(userId))
                .enqueue(new Callback<com.example.trekmatenepal.model.BasicApiResponse>() {
                    @Override public void onResponse(Call<com.example.trekmatenepal.model.BasicApiResponse> call, Response<com.example.trekmatenepal.model.BasicApiResponse> response) { }
                    @Override public void onFailure(Call<com.example.trekmatenepal.model.BasicApiResponse> call, Throwable t) { }
                });
    }
}
