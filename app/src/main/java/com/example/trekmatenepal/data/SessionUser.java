package com.example.trekmatenepal.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SessionUser — the identity of whoever is using the app right now.
 *
 * There is no real auth yet, so the id is simply the email typed at login
 * (stored in TrekMatePrefs). It is used as the "user / seller id" that gear
 * listings and rental notifications are addressed to.
 */
public final class SessionUser {

    private static final String PREFS       = "TrekMatePrefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String DEFAULT_ID  = "You";

    private SessionUser() { }

    /** Current user / seller id. Never empty. */
    public static String getUserId(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String id = prefs.getString(KEY_USER_ID, DEFAULT_ID);
        return (id == null || id.trim().isEmpty()) ? DEFAULT_ID : id.trim();
    }

    /** Called on login so gear + notifications can be addressed to this user. */
    public static void setUserId(Context ctx, String userId) {
        if (userId == null || userId.trim().isEmpty()) userId = DEFAULT_ID;
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
           .edit()
           .putString(KEY_USER_ID, userId.trim())
           .apply();
    }

    /** True when the given seller/user id belongs to the person using the app. */
    public static boolean isMe(Context ctx, String id) {
        if (id == null) return false;
        String me = getUserId(ctx);
        return me.equalsIgnoreCase(id.trim()) || DEFAULT_ID.equalsIgnoreCase(id.trim());
    }
}
