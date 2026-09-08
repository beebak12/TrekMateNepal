package com.example.trekmatenepal.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.example.trekmatenepal.api.ApiClient;
import com.example.trekmatenepal.api.ApiService;
import com.example.trekmatenepal.model.GearListResponse;
import com.example.trekmatenepal.model.GearMutationResponse;
import com.example.trekmatenepal.model.GearRequest;
import com.example.trekmatenepal.models.RentalGearModel;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class GearBackendRepository {
    public interface ListCallback { void success(ArrayList<RentalGearModel> gear); void error(String message); }
    public interface ActionCallback { void success(RentalGearModel gear); void error(String message); }
    private GearBackendRepository() { }
    private static ApiService api() { return ApiClient.getClient().create(ApiService.class); }
    private static String bearer(Context context) { return "Bearer " + SessionUser.getToken(context); }

    public static void getAll(ListCallback callback) { enqueueList(api().getGears(), callback); }
    public static void getMine(Context context, ListCallback callback) {
        enqueueList(api().getMyGears(bearer(context)), callback);
    }
    private static void enqueueList(Call<GearListResponse> call, ListCallback callback) {
        call.enqueue(new Callback<GearListResponse>() {
            @Override public void onResponse(Call<GearListResponse> call, Response<GearListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    ArrayList<RentalGearModel> result = new ArrayList<>();
                    if (response.body().getData() != null) for (GearListResponse.GearData data : response.body().getData()) result.add(map(data));
                    callback.success(result);
                } else callback.error(message(response, "Unable to load gear."));
            }
            @Override public void onFailure(Call<GearListResponse> call, Throwable t) { callback.error("Cannot connect to the server."); }
        });
    }

    public static void save(Context context, RentalGearModel gear, Uri image, ActionCallback callback) {
        GearRequest request = request(gear);
        boolean editing = gear.getId().matches("\\d+");
        Call<GearMutationResponse> call = editing
                ? api().updateGear(bearer(context), gear.getId(), request)
                : api().createGear(bearer(context), request);
        call.enqueue(new Callback<GearMutationResponse>() {
            @Override public void onResponse(Call<GearMutationResponse> call, Response<GearMutationResponse> response) {
                if (!response.isSuccessful() || response.body() == null || !response.body().isSuccess()) {
                    callback.error(message(response, "Unable to save gear.")); return;
                }
                if (!editing && response.body().getData() != null) gear.setId(String.valueOf(response.body().getData().getId()));
                if (image == null) { callback.success(gear); return; }
                uploadImage(context, gear, image, callback);
            }
            @Override public void onFailure(Call<GearMutationResponse> call, Throwable t) { callback.error("Cannot connect to the server."); }
        });
    }

    private static void uploadImage(Context context, RentalGearModel gear, Uri uri, ActionCallback callback) {
        try {
            byte[] bytes = imageBytes(context, uri);
            RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
            MultipartBody.Part part = MultipartBody.Part.createFormData("image", "gear.jpg", body);
            api().uploadGearImage(bearer(context), gear.getId(), part).enqueue(new Callback<GearMutationResponse>() {
                @Override public void onResponse(Call<GearMutationResponse> call, Response<GearMutationResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        if (response.body().getData() != null) gear.setRemoteImageUrl(response.body().getData().getImageUrl());
                        callback.success(gear);
                    } else callback.error(message(response, "Gear was saved, but its image could not be uploaded."));
                }
                @Override public void onFailure(Call<GearMutationResponse> call, Throwable t) { callback.error("Gear was saved, but its image could not be uploaded."); }
            });
        } catch (Exception error) { callback.error("The selected gear image could not be read."); }
    }

    public static void delete(Context context, RentalGearModel gear, ActionCallback callback) {
        api().deleteGear(bearer(context), gear.getId()).enqueue(actionCallback(gear, callback, "Unable to delete gear."));
    }
    public static void updateStatus(Context context, RentalGearModel gear, ActionCallback callback) {
        api().updateGear(bearer(context), gear.getId(), request(gear)).enqueue(actionCallback(gear, callback, "Unable to update availability."));
    }
    private static Callback<GearMutationResponse> actionCallback(RentalGearModel gear, ActionCallback callback, String fallback) {
        return new Callback<GearMutationResponse>() {
            @Override public void onResponse(Call<GearMutationResponse> call, Response<GearMutationResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) callback.success(gear);
                else callback.error(message(response, fallback));
            }
            @Override public void onFailure(Call<GearMutationResponse> call, Throwable t) { callback.error("Cannot connect to the server."); }
        };
    }
    private static GearRequest request(RentalGearModel gear) {
        double price;
        try { price = Double.parseDouble(gear.getPriceRaw().replace(",", "")); } catch (Exception e) { price = 0; }
        return new GearRequest(gear.getCategory(), gear.getName(), gear.getDescription(), price,
                gear.getAvailability().toLowerCase(Locale.ROOT), gear.getCondition(), gear.getSize(), gear.getLocation());
    }
    private static RentalGearModel map(GearListResponse.GearData data) {
        String raw = data.getPrice() == Math.rint(data.getPrice()) ? String.valueOf((long) data.getPrice()) : String.valueOf(data.getPrice());
        RentalGearModel gear = new RentalGearModel(0, null, data.getName(), data.getCategory(), "New",
                "Rs. " + raw + " / week", raw, title(data.getAvailability()), data.getLocation(),
                data.getDescription(), data.getSize(), data.getCondition(), data.getOwnerName(), String.valueOf(data.getOwnerUserId()));
        gear.setId(String.valueOf(data.getId())); gear.setRemoteImageUrl(data.getImageUrl()); return gear;
    }
    private static String title(String value) { return value == null || value.isEmpty() ? "Available" : Character.toUpperCase(value.charAt(0)) + value.substring(1); }
    private static String message(Response<?> response, String fallback) { return fallback + " (" + response.code() + ")"; }
    private static byte[] imageBytes(Context context, Uri uri) throws Exception {
        Bitmap bitmap;
        try (InputStream input = context.getContentResolver().openInputStream(uri)) { bitmap = BitmapFactory.decodeStream(input); }
        if (bitmap == null) throw new IllegalArgumentException("Unsupported image");
        float scale = Math.min(1f, 1280f / Math.max(bitmap.getWidth(), bitmap.getHeight()));
        Bitmap resized = scale < 1 ? Bitmap.createScaledBitmap(bitmap, Math.round(bitmap.getWidth()*scale), Math.round(bitmap.getHeight()*scale), true) : bitmap;
        ByteArrayOutputStream output = new ByteArrayOutputStream(); resized.compress(Bitmap.CompressFormat.JPEG, 82, output);
        if (resized != bitmap) resized.recycle(); bitmap.recycle(); return output.toByteArray();
    }
}
