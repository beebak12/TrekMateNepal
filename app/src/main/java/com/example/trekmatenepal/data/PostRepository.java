package com.example.trekmatenepal.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.PostModel;
import com.example.trekmatenepal.models.JoinRequestModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PostRepository {
    private static final String PREF_NAME = "TrekMatePosts";
    private static final String KEY_POSTS = "all_posts_json";
    private static final String KEY_REQUESTS = "join_requests_json";

    private static List<PostModel> posts = new ArrayList<>();

    public static List<PostModel> getAllPosts() {
        return posts;
    }

    public static void loadPosts(Context context) {
        posts.clear();
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_POSTS, null);
        
        if (json != null) {
            try {
                JSONArray arr = new JSONArray(json);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    PostModel p = new PostModel(
                        o.getString("title"),
                        o.getString("author"),
                        o.getString("location"),
                        o.getString("dateRange"),
                        o.getString("duration"),
                        o.getString("interestedCount"),
                        o.getInt("imageRes")
                    );
                    p.setId(o.getString("id"));
                    p.setAuthorId(o.optString("authorId", "You"));
                    p.setDescription(o.optString("description", ""));
                    p.setBudget(o.optString("budget", ""));
                    p.setExperienceLevel(o.optString("experienceLevel", ""));
                    p.setCustomImageUri(o.optString("customImageUri", null));
                    p.setGroupId(o.optString("groupId", "group_" + p.getId()));
                    posts.add(p);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Seed initial data if empty
            posts.add(new PostModel("Looking for partner for Everest Base Camp", "Aayush Rai", "Kathmandu", "20 May – 2 Jun 2025", "12 Days", "2", R.drawable.everest));
            posts.add(new PostModel("Need trekking partner for Annapurna Circuit", "Sushmita Gurung", "Pokhara", "15 May – 26 May 2025", "12 Days", "1", R.drawable.annapurna));
            posts.add(new PostModel("Partner wanted for Langtang Valley Trek", "Bikash Thapa", "Kathmandu", "18 May – 24 May 2025", "7 Days", "3", R.drawable.langtang));
            savePosts(context);
        }
    }

    public static void addPost(Context context, PostModel post) {
        posts.add(0, post);
        savePosts(context);
    }

    private static void savePosts(Context context) {
        try {
            JSONArray arr = new JSONArray();
            for (PostModel p : posts) {
                JSONObject o = new JSONObject();
                o.put("id", p.getId());
                o.put("title", p.getTitle());
                o.put("author", p.getAuthor());
                o.put("authorId", p.getAuthorId());
                o.put("location", p.getLocation());
                o.put("dateRange", p.getDateRange());
                o.put("duration", p.getDuration());
                o.put("interestedCount", p.getInterestedCount());
                o.put("imageRes", p.getImageRes());
                o.put("customImageUri", p.getCustomImageUri());
                o.put("description", p.getDescription());
                o.put("budget", p.getBudget());
                o.put("experienceLevel", p.getExperienceLevel());
                o.put("groupId", p.getGroupId());
                arr.put(o);
            }
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                   .edit().putString(KEY_POSTS, arr.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addJoinRequest(Context context, JoinRequestModel request) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            JSONArray arr = new JSONArray(prefs.getString(KEY_REQUESTS, "[]"));
            JSONObject o = new JSONObject();
            o.put("id", request.getId());
            o.put("postId", request.getPostId());
            o.put("requesterId", request.getRequesterId());
            o.put("requesterName", request.getRequesterName());
            o.put("status", request.getStatus());
            o.put("timestamp", request.getTimestamp());
            arr.put(o);
            prefs.edit().putString(KEY_REQUESTS, arr.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
