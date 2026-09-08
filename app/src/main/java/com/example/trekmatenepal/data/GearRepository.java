package com.example.trekmatenepal.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.trekmatenepal.models.RentalGearModel;
import com.example.trekmatenepal.R;

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
                RentalGearModel loaded = list.get(list.size() - 1);
                loaded.setId(o.optString("id", legacyId(loaded)));
                loaded.setRemoteImageUrl(o.optString("remoteImageUrl", ""));
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
            arr.put(0, toJson(gear)); // Add at the top
            prefs.edit().putString(KEY_USER_GEAR, arr.toString()).apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to save gear", e);
        }
    }

    public static boolean updateGear(Context ctx, RentalGearModel updated) {
        ArrayList<RentalGearModel> gear = getUserGear(ctx);
        for (int i = 0; i < gear.size(); i++) {
            if (gear.get(i).getId().equals(updated.getId())) {
                gear.set(i, updated);
                saveAll(ctx, gear);
                return true;
            }
        }
        return false;
    }

    public static boolean deleteGear(Context ctx, String gearId) {
        ArrayList<RentalGearModel> gear = getUserGear(ctx);
        for (int i = 0; i < gear.size(); i++) {
            if (gear.get(i).getId().equals(gearId)) {
                gear.remove(i);
                saveAll(ctx, gear);
                return true;
            }
        }
        return false;
    }

    public static ArrayList<RentalGearModel> getAllGear(Context ctx) {
        ArrayList<RentalGearModel> all = getUserGear(ctx);
        all.addAll(seedGear());
        return all;
    }

    private static void saveAll(Context ctx, ArrayList<RentalGearModel> gear) {
        try {
            JSONArray arr = new JSONArray();
            for (RentalGearModel item : gear) arr.put(toJson(item));
            ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                    .putString(KEY_USER_GEAR, arr.toString()).apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to update gear", e);
        }
    }

    private static JSONObject toJson(RentalGearModel gear) throws Exception {
        JSONObject o = new JSONObject();
        o.put("id", gear.getId());
        o.put("image", gear.getImage());
        o.put("customImageUri", gear.getCustomImageUri());
        o.put("remoteImageUrl", gear.getRemoteImageUrl());
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
        return o;
    }

    private static String legacyId(RentalGearModel gear) {
        return "legacy_" + Math.abs((gear.getSellerId() + "|" + gear.getName() + "|"
                + gear.getPriceRaw() + "|" + gear.getLocation()).hashCode());
    }

    private static ArrayList<RentalGearModel> seedGear() {
        ArrayList<RentalGearModel> list = new ArrayList<>();
        list.add(seed(R.drawable.jacket, "Down Jacket", "Clothing", "4.8", "Rs. 2,000 / week", "2000", "Kathmandu", "Warm and lightweight trekking down jacket suitable for high altitude trekking in Nepal.", "M / L / XL", "Excellent", "Trek Gear Nepal"));
        list.add(seed(R.drawable.sleepingbag, "Sleeping Bag", "Sleeping", "4.7", "Rs. 1,000 / week", "1000", "Kathmandu", "3-season sleeping bag rated to -10°C, perfect for Nepal trekking.", "", "Good", "Himalaya Rentals"));
        list.add(seed(R.drawable.backpack, "Trekking Backpack", "Bags", "4.9", "Rs. 1,500 / week", "1500", "Pokhara", "60L trekking backpack with rain cover and ergonomic back support.", "", "Excellent", "Nepal Trek Store"));
        list.add(seed(R.drawable.boots, "Trekking Boots", "Footwear", "4.8", "Rs. 1,800 / week", "1800", "Kathmandu", "Waterproof ankle-support trekking boots, ideal for rocky terrain.", "40–45", "Good", "Trek Gear Nepal"));
        list.add(seed(R.drawable.poles, "Trekking Pole", "Trekking Tools", "4.6", "Rs. 500 / week", "500", "Kathmandu", "Lightweight aluminum trekking pole with rubber tip, adjustable height.", "", "Good", "Himalaya Rentals"));
        list.add(seed(R.drawable.tent, "Camping Tent", "Camping", "4.7", "Rs. 2,500 / week", "2500", "Pokhara", "2-person waterproof camping tent, wind resistant up to 60 km/h.", "", "Excellent", "Nepal Trek Store"));
        list.add(seed(R.drawable.headlamp, "Headlamp", "Trekking Tools", "4.5", "Rs. 300 / week", "300", "Kathmandu", "200 lumen rechargeable LED headlamp with adjustable beam.", "", "Good", "Trek Gear Nepal"));
        list.add(seed(R.drawable.jacket, "Rain Jacket", "Clothing", "4.7", "Rs. 1,200 / week", "1200", "Kathmandu", "Waterproof and breathable rain jacket with sealed seams.", "S / M / L / XL", "Excellent", "Himalaya Rentals"));
        list.add(seed(R.drawable.gloves, "Trekking Gloves", "Clothing", "4.4", "Rs. 400 / week", "400", "Kathmandu", "Insulated trekking gloves with touchscreen-compatible fingertips.", "S / M / L", "Good", "Trek Gear Nepal"));
        list.add(seed(R.drawable.backpack, "Duffel Bag", "Bags", "4.5", "Rs. 800 / week", "800", "Pokhara", "80L duffel bag with padlock and waterproof lining, ideal for porters.", "", "Good", "Nepal Trek Store"));
        return list;
    }

    private static RentalGearModel seed(int image, String name, String category, String rating,
                                        String price, String rawPrice, String location, String description,
                                        String size, String condition, String seller) {
        RentalGearModel gear = new RentalGearModel(image, name, category, rating, price, rawPrice,
                "Available", location, description, size, condition, seller);
        gear.setId("seed_" + Math.abs((seller + "|" + name).hashCode()));
        return gear;
    }
}
