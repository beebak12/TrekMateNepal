package com.example.trekmatenepal.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.trekmatenepal.models.CartItemModel;
import com.example.trekmatenepal.models.NotificationModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * NotificationRepository — stores in-app notifications addressed to a user or
 * seller id, persisted as a JSON array in SharedPreferences.
 *
 * Every gear rental writes two notifications per item: one to the seller who
 * owns the gear, and one to the trekker who booked it.
 */
public final class NotificationRepository {

    private static final String TAG            = "NotificationRepo";
    private static final String PREFS          = "TrekMateNotifications";
    private static final String KEY_ALL        = "all_notifications";
    private static final int    MAX_STORED     = 200;

    private static final SimpleDateFormat TIME_FMT =
            new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());

    private NotificationRepository() { }

    // ── Read ─────────────────────────────────────────────────────────────────
    /** Notifications addressed to the given user / seller id, newest first. */
    public static ArrayList<NotificationModel> getFor(Context ctx, String recipientId) {
        ArrayList<NotificationModel> out = new ArrayList<>();
        if (recipientId == null) return out;

        for (NotificationModel n : readAll(ctx)) {
            if (recipientId.equalsIgnoreCase(n.getRecipientId())) out.add(n);
        }
        Collections.sort(out, new Comparator<NotificationModel>() {
            @Override public int compare(NotificationModel a, NotificationModel b) {
                return Long.compare(b.getTimestamp(), a.getTimestamp());
            }
        });
        return out;
    }

    private static ArrayList<NotificationModel> readAll(Context ctx) {
        ArrayList<NotificationModel> list = new ArrayList<>();
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        try {
            JSONArray arr = new JSONArray(prefs.getString(KEY_ALL, "[]"));
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                list.add(new NotificationModel(
                        o.optString("recipientId"),
                        o.optString("title"),
                        o.optString("message"),
                        o.optString("timeLabel"),
                        o.optLong("timestamp", 0L),
                        o.optString("bookingId"),
                        o.optString("type", "rental")));
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to read notifications", e);
        }
        return list;
    }

    // ── Write ────────────────────────────────────────────────────────────────
    /** Adds one notification for one recipient id. */
    public static void notifyUser(Context ctx, String recipientId, String title,
                                  String message, String bookingId, String type) {
        if (recipientId == null || recipientId.trim().isEmpty()) return;

        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        try {
            JSONArray arr = new JSONArray(prefs.getString(KEY_ALL, "[]"));
            long now = System.currentTimeMillis();

            JSONObject o = new JSONObject();
            o.put("recipientId", recipientId.trim());
            o.put("title", title);
            o.put("message", message);
            o.put("timeLabel", TIME_FMT.format(new Date(now)));
            o.put("timestamp", now);
            o.put("bookingId", bookingId != null ? bookingId : "");
            o.put("type", type != null ? type : "rental");
            arr.put(o);

            // Keep the store bounded — drop the oldest entries.
            while (arr.length() > MAX_STORED) arr.remove(0);

            prefs.edit().putString(KEY_ALL, arr.toString()).apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to save notification", e);
        }
    }

    /**
     * Fans a confirmed rental out to everyone involved: each gear's seller id
     * gets a "your gear was rented" notice, and the renter gets a confirmation.
     */
    public static void notifyRentalConfirmed(Context ctx, List<CartItemModel> items,
                                             String bookingId, String renterId,
                                             int totalAmount) {
        if (items == null || items.isEmpty()) return;

        // ── Each seller / owner id ───────────────────────────────────────────
        for (CartItemModel item : items) {
            String qtyLabel = item.getQuantity() + " × " + item.getGearName();
            notifyUser(ctx,
                    item.getSeller(),
                    "Your gear was rented",
                    qtyLabel + " was booked by " + renterId + " for "
                            + item.getDates() + ". Pickup: " + item.getPickupLocation()
                            + ". You earn Rs. " + String.format(Locale.getDefault(), "%,d", item.getSubtotal()) + ".",
                    bookingId,
                    "rental");
        }

        // ── The trekker who booked ───────────────────────────────────────────
        StringBuilder names = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) names.append(i == items.size() - 1 ? " and " : ", ");
            names.append(items.get(i).getQuantity()).append(" × ").append(items.get(i).getGearName());
        }
        notifyUser(ctx,
                renterId,
                "Booking confirmed",
                "Your rental of " + names + " is confirmed. Total Rs. "
                        + String.format(Locale.getDefault(), "%,d", totalAmount)
                        + ". Booking ID " + bookingId + ".",
                bookingId,
                "rental");
    }

    /** Confirms to a seller that their newly posted gear is live. */
    public static void notifyGearListed(Context ctx, String sellerId, String gearName) {
        notifyUser(ctx, sellerId,
                "Your gear is now listed",
                gearName + " is live in Gear Rental. You'll be notified here as soon as someone rents it.",
                "", "listing");
    }

    /** Notifies the post admin about a new join request. */
    public static void notifyJoinRequest(Context ctx, String adminId, String requesterName, String postTitle) {
        notifyUser(ctx, adminId,
                "New Trek Join Request",
                requesterName + " wants to join your trek: " + postTitle,
                "", "partner");
    }

    /** Removes every notification addressed to one recipient id, leaving others intact. */
    public static void clearFor(Context ctx, String recipientId) {
        if (recipientId == null) return;

        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        try {
            JSONArray arr  = new JSONArray(prefs.getString(KEY_ALL, "[]"));
            JSONArray kept = new JSONArray();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                if (!recipientId.equalsIgnoreCase(o.optString("recipientId"))) kept.put(o);
            }
            prefs.edit().putString(KEY_ALL, kept.toString()).apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to clear notifications", e);
        }
    }
}
