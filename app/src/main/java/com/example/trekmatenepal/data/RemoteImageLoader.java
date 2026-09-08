package com.example.trekmatenepal.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import com.example.trekmatenepal.api.ApiClient;
import com.example.trekmatenepal.api.ApiService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class RemoteImageLoader {
    private RemoteImageLoader() { }
    public static void load(ImageView view, String path, int fallback) {
        view.setImageResource(fallback);
        if (path == null || path.trim().isEmpty()) return;
        String url = path.startsWith("http") ? path : ApiClient.getBaseUrl() + path.replaceFirst("^/", "");
        view.setTag(url);
        ApiClient.getClient().create(ApiService.class).downloadImage(url).enqueue(new Callback<ResponseBody>() {
            @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null && url.equals(view.getTag())) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    if (bitmap != null) view.setImageBitmap(bitmap);
                }
            }
            @Override public void onFailure(Call<ResponseBody> call, Throwable throwable) { }
        });
    }
}
