package com.example.trekmatenepal.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.trekmatenepal.models.RentalGearModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * GearRepository — lightweight store for user-posted rental gear.
 */
public final class GearRepository {

    private static final String TAG = "GearRepository";
    private static final String PREFS = "TrekMateGear";
    private static final String KEY_USER_GEAR = "user_posted_gear_v2"; // V2 to support custom images/sizes

    private GearRepository() { }

    /** Returns all user-posted gear, newest first. */
    public static ArrayList<RentalGearModel> getUserGear(Context ctx) {
        ArrayList<RentalGearModel> list = new ArrayList<>();
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_USER_GEAR, "[]");
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                list.add(new RentalGearModel(
                        o.optInt("image", 0),
                        o.optString("customImageUri", null),
                        o.optString("name"),
                        o.optString("category"),
                        o.optString("rating", "New"),
                        o.optString("price"),
                        o.optString("priceRaw", "0"),
                        o.optString("availability", "Available"),
                        o.optString("location"),
                        o.optString("description"),
                        o.optString("size"),
                        o.optString("condition"),
                        o.optString("seller", "You"),
                        o.optString("sellerId", "")));
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to read user gear", e);
        }
        return list;
    }

    /** Appends a newly posted gear item to the persisted list. */
    public static void addGear(Context ctx, RentalGearModel gear) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_USER_GEAR, "[]");
        try {
            JSONArray arr = new JSONArray(json);
            JSONObject o = new JSONObject();
            o.put("image", gear.getImage());
            o.put("customImageUri", gear.getCustomImageUri());
            o.put("name", gear.getName());
            o.put("category", gear.getCategory());
            o.put("rating", gear.getRating());
            o.put("price", gear.getPrice());
            o.put("priceRaw", gear.getPriceRaw());
            o.put("availability", gear.getAvailability());
            o.put("location", gear.getLocation());
            o.put("description", gear.getDescription());
            o.put("size", gear.getSize());
            o.put("condition", gear.getCondition());
            o.put("seller", gear.getSeller());
            o.put("sellerId", gear.getSellerId());
            
            arr.put(0, o); // Add at the top
            prefs.edit().putString(KEY_USER_GEAR, arr.toString()).apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to save gear", e);
        }
    }
}
