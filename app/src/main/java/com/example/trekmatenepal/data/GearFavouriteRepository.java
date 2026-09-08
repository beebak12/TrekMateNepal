package com.example.trekmatenepal.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.trekmatenepal.models.RentalGearModel;
import com.example.trekmatenepal.api.ApiClient;
import com.example.trekmatenepal.api.ApiService;
import com.example.trekmatenepal.model.BasicApiResponse;
import com.example.trekmatenepal.model.FavoriteListResponse;
import com.example.trekmatenepal.model.FavoriteRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                syncRemote(context, gear, false);
                return false;
            }
        }
        saved.add(0, gear);
        save(context, saved);
        syncRemote(context, gear, true);
        return true;
    }

    /** Pushes a favourite change to the authenticated account without blocking the UI. */
    private static void syncRemote(Context context, RentalGearModel gear, boolean selected) {
        String token = SessionUser.getToken(context);
        if (token == null || token.trim().isEmpty() || !gear.getId().matches("\\d+")) return;
        ApiService api = ApiClient.getClient().create(ApiService.class);
        String auth = "Bearer " + token;
        Call<BasicApiResponse> call = selected
                ? api.addFavorite(auth, new FavoriteRequest("gear", Integer.parseInt(gear.getId())))
                : api.removeFavorite(auth, "gear", Integer.parseInt(gear.getId()));
        call.enqueue(new Callback<BasicApiResponse>() {
            @Override public void onResponse(Call<BasicApiResponse> call, Response<BasicApiResponse> response) {
                // Local state remains available when the device is temporarily offline.
            }
            @Override public void onFailure(Call<BasicApiResponse> call, Throwable t) { }
        });
    }

    /** Reconciles the local cache with server favourites for the supplied server gear list. */
    public static void syncFromServer(Context context, List<RentalGearModel> availableGear, Runnable onComplete) {
        String token = SessionUser.getToken(context);
        if (token == null || token.trim().isEmpty()) { if (onComplete != null) onComplete.run(); return; }
        ApiClient.getClient().create(ApiService.class).getFavorites("Bearer " + token)
                .enqueue(new Callback<FavoriteListResponse>() {
                    @Override public void onResponse(Call<FavoriteListResponse> call, Response<FavoriteListResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            ArrayList<RentalGearModel> server = new ArrayList<>();
                            if (response.body().getData() != null) for (FavoriteListResponse.FavoriteItem item : response.body().getData()) {
                                if (!"gear".equalsIgnoreCase(item.getEntityType())) continue;
                                for (RentalGearModel gear : availableGear) if (gear.getId().equals(String.valueOf(item.getEntityId()))) { server.add(gear); break; }
                            }
                            save(context, server);
                        }
                        if (onComplete != null) onComplete.run();
                    }
                    @Override public void onFailure(Call<FavoriteListResponse> call, Throwable t) { if (onComplete != null) onComplete.run(); }
                });
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

    private static RentalGearModel fromJson(JSONObject o) {
        RentalGearModel gear = new RentalGearModel(o.optInt("image", 0), o.optString("customImageUri", null),
                o.optString("name"), o.optString("category"), o.optString("rating"),
                o.optString("price"), o.optString("priceRaw", "0"),
                o.optString("availability"), o.optString("location"),
                o.optString("description"), o.optString("size"),
                o.optString("condition"), o.optString("seller"), o.optString("sellerId"));
        gear.setRemoteImageUrl(o.optString("remoteImageUrl", ""));
        return gear;
    }
}
