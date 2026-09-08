package com.example.trekmatenepal.data;

import android.content.Context;
import com.example.trekmatenepal.api.ApiClient;
import com.example.trekmatenepal.api.ApiService;
import com.example.trekmatenepal.model.BasicApiResponse;
import com.example.trekmatenepal.model.PartnerPostListResponse;
import com.example.trekmatenepal.model.PartnerPostRequest;
import com.example.trekmatenepal.model.PartnerPostResponse;
import com.example.trekmatenepal.models.PostModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Server persistence for trek-partner posts and join requests. */
public final class PartnerPostBackendRepository {
    public interface CallbackResult { void success(); void error(String message); }
    public interface ListCallback { void success(ArrayList<PostModel> posts); void error(String message); }
    private PartnerPostBackendRepository() { }

    public static void create(Context context, PostModel post, String startDate, int required, CallbackResult callback) {
        String token = SessionUser.getToken(context);
        if (token == null || token.trim().isEmpty()) { callback.error("Not signed in to the server"); return; }
        String date = toSqlDate(startDate);
        if (date == null) { callback.error("Invalid travel date"); return; }
        String exp = post.getExperienceLevel();
        if ("Moderate".equalsIgnoreCase(exp)) exp = "Intermediate";
        if ("Expert".equalsIgnoreCase(exp)) exp = "Experienced";
        PartnerPostRequest request = new PartnerPostRequest(null, required, date, post.getDuration(), exp, "Any", post.getGroupName(), post.getDescription());
        ApiClient.getClient().create(ApiService.class).createPartnerPost("Bearer " + token, request)
                .enqueue(new Callback<PartnerPostResponse>() {
                    @Override public void onResponse(Call<PartnerPostResponse> call, Response<PartnerPostResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            if (response.body().getData() != null) post.setId(String.valueOf(response.body().getData().getId()));
                            callback.success();
                        } else callback.error("Server could not save the partner post (" + response.code() + ")");
                    }
                    @Override public void onFailure(Call<PartnerPostResponse> call, Throwable t) { callback.error("Partner post saved locally; server is unavailable"); }
                });
    }

    public static void request(Context context, String postId, String message, CallbackResult callback) {
        String token = SessionUser.getToken(context);
        if (token == null || token.trim().isEmpty() || !postId.matches("\\d+")) { callback.error("Not available for local posts"); return; }
        java.util.HashMap<String, String> body = new java.util.HashMap<>(); body.put("message", message == null ? "" : message);
        ApiClient.getClient().create(ApiService.class).requestPartner("Bearer " + token, Integer.parseInt(postId), body)
                .enqueue(new Callback<BasicApiResponse>() {
                    @Override public void onResponse(Call<BasicApiResponse> call, Response<BasicApiResponse> response) { if (response.isSuccessful()) callback.success(); else callback.error("Request was not accepted by server"); }
                    @Override public void onFailure(Call<BasicApiResponse> call, Throwable t) { callback.error("Request saved locally; server is unavailable"); }
                });
    }

    public static void getAll(ListCallback callback) {
        ApiClient.getClient().create(ApiService.class).getPartnerPosts().enqueue(new Callback<PartnerPostListResponse>() {
            @Override public void onResponse(Call<PartnerPostListResponse> call, Response<PartnerPostListResponse> response) {
                if (!response.isSuccessful() || response.body() == null || !response.body().isSuccess()) { callback.error("Unable to load partner posts"); return; }
                ArrayList<PostModel> result = new ArrayList<>();
                if (response.body().getData() != null) for (PartnerPostListResponse.PartnerPostData r : response.body().getData()) {
                    String title = "Trek partner request";
                    PostModel p = new PostModel(title, r.author_name == null ? "Trek user " + r.user_id : r.author_name, "Nepal", r.travel_date == null ? "" : r.travel_date,
                            r.expected_duration == null ? "" : r.expected_duration, String.valueOf(r.required_partners), 0);
                    p.setId(String.valueOf(r.id)); p.setAuthorId(String.valueOf(r.user_id)); p.setDescription(r.description); p.setExperienceLevel(r.experience_level);
                    p.setGroupName(r.group_name == null ? p.getTitle() + " Group" : r.group_name);
                    if (r.group_id > 0) p.setGroupId(String.valueOf(r.group_id));
                    result.add(p);
                }
                callback.success(result);
            }
            @Override public void onFailure(Call<PartnerPostListResponse> call, Throwable t) { callback.error("Unable to connect to server"); }
        });
    }

    private static String toSqlDate(String value) {
        try { Date d = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH).parse(value.trim()); return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(d); }
        catch (Exception e) { return null; }
    }
}
