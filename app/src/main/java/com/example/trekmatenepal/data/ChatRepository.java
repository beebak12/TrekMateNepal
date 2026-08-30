package com.example.trekmatenepal.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.trekmatenepal.R;
import com.example.trekmatenepal.models.ChatSummaryModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ChatRepository {
    private static final String PREF_NAME = "TrekMateChats";
    private static final String KEY_CHATS = "all_chats_json";

    private static List<ChatSummaryModel> chats = new ArrayList<>();

    public static List<ChatSummaryModel> getChats(boolean isGroup) {
        List<ChatSummaryModel> filtered = new ArrayList<>();
        for (ChatSummaryModel c : chats) {
            if (c.isGroup() == isGroup) filtered.add(c);
        }
        return filtered;
    }

    public static void loadChats(Context context) {
        chats.clear();
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_CHATS, null);
        
        if (json != null) {
            try {
                JSONArray arr = new JSONArray(json);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    ChatSummaryModel c = new ChatSummaryModel(
                        o.getString("id"),
                        o.getString("name"),
                        o.getString("lastMessage"),
                        o.getString("time"),
                        o.getInt("imageRes"),
                        o.optInt("unreadCount", 0),
                        o.getBoolean("isGroup")
                    );
                    c.setCustomImageUri(o.optString("customImageUri", null));
                    chats.add(c);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Seed initial data
            chats.add(new ChatSummaryModel("1", "Sandeep Magar", "Are you joining the trek?", "10:30 AM", R.drawable.partner1, 1, false));
            chats.add(new ChatSummaryModel("2", "Pema Sherpa", "Okay, see you tomorrow.", "9:45 AM", R.drawable.partner2, 0, false));
            chats.add(new ChatSummaryModel("group_ebc", "Everest Base Camp Group", "Nirajan: Let's start at 6 AM", "8:20 AM", R.drawable.everest, 2, true));
            saveChats(context);
        }
    }

    public static void addChat(Context context, ChatSummaryModel chat) {
        // Avoid duplicates by ID
        for (int i = 0; i < chats.size(); i++) {
            if (chats.get(i).getId().equals(chat.getId())) {
                chats.set(i, chat);
                saveChats(context);
                return;
            }
        }
        chats.add(0, chat);
        saveChats(context);
    }

    public static void saveChats(Context context) {
        try {
            JSONArray arr = new JSONArray();
            for (ChatSummaryModel c : chats) {
                JSONObject o = new JSONObject();
                o.put("id", c.getId());
                o.put("name", c.getName());
                o.put("lastMessage", c.getLastMessage());
                o.put("time", c.getTime());
                o.put("imageRes", c.getImageRes());
                o.put("unreadCount", c.getUnreadCount());
                o.put("isGroup", c.isGroup());
                o.put("customImageUri", c.getCustomImageUri());
                arr.put(o);
            }
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                   .edit().putString(KEY_CHATS, arr.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getTotalUnreadCount() {
        int count = 0;
        for (ChatSummaryModel c : chats) {
            count += c.getUnreadCount();
        }
        return count;
    }
}
