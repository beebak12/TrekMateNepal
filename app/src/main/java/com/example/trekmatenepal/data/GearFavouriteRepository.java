package com.example.trekmatenepal.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.trekmatenepal.models.RentalGearModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/** Persistent, per-account favourite gear storage. */
public final class GearFavouriteRepository {
    private static final String TAG = "GearFavourites";
    private static final String PREFS = "TrekMateGearFavourites";

    private GearFavouriteRepository() { }

    private static String key(Context context) {
        return "favourites_" + SessionUser.getUserId(context).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_-]", "_");
    }

    public static String idFor(RentalGearModel gear) {
        return (gear.getSellerId() + "|" + gear.getSeller() + "|" + gear.getName()
                + "|" + gear.getPriceRaw() + "|" + gear.getLocation()).toLowerCase(Locale.ROOT);
    }

    public static boolean isFavourite(Context context, RentalGearModel gear) {
        String wanted = idFor(gear);
        for (RentalGearModel saved : getFavourites(context)) {
            if (wanted.equals(idFor(saved))) return true;
        }
        return false;
    }

    /** Toggles the item and returns its new favourite state. */
    public static boolean toggle(Context context, RentalGearModel gear) {
        ArrayList<RentalGearModel> saved = getFavourites(context);
        String wanted = idFor(gear);
        for (int i = 0; i < saved.size(); i++) {
            if (wanted.equals(idFor(saved.get(i)))) {
                saved.remove(i);
                save(context, saved);
                return false;
            }
        }
        saved.add(0, gear);
        save(context, saved);
        return true;
    }

    public static ArrayList<RentalGearModel> getFavourites(Context context) {
        ArrayList<RentalGearModel> result = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        try {
            JSONArray array = new JSONArray(prefs.getString(key(context), "[]"));
            for (int i = 0; i < array.length(); i++) result.add(fromJson(array.getJSONObject(i)));
        } catch (Exception error) {
            Log.e(TAG, "Unable to read favourite gear", error);
        }
        return result;
    }

    private static void save(Context context, ArrayList<RentalGearModel> gear) {
        JSONArray array = new JSONArray();
        try {
            for (RentalGearModel item : gear) array.put(toJson(item));
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
                    .putString(key(context), array.toString()).apply();
        } catch (Exception error) {
            Log.e(TAG, "Unable to save favourite gear", error);
        }
    }

    private static JSONObject toJson(RentalGearModel gear) throws Exception {
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
        return o;
    }

    private static RentalGearModel fromJson(JSONObject o) {
        return new RentalGearModel(o.optInt("image", 0), o.optString("customImageUri", null),
                o.optString("name"), o.optString("category"), o.optString("rating"),
                o.optString("price"), o.optString("priceRaw", "0"),
                o.optString("availability"), o.optString("location"),
                o.optString("description"), o.optString("size"),
                o.optString("condition"), o.optString("seller"), o.optString("sellerId"));
    }
}
