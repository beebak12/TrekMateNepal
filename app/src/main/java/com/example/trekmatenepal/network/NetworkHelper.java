package com.example.trekmatenepal.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkHelper {

    private static final String BASE_URL = "http://10.0.2.2:5000/"; // Default Node.js server address
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface Callback {
        void onSuccess(JSONObject response);
        void onError(String error);
    }

    public static void post(String endpoint, JSONObject body, Callback callback) {
        executor.execute(() -> {
            try {
                URL url = new URL(BASE_URL + endpoint);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                if (body != null) {
                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = body.toString().getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                }

                int code = conn.getResponseCode();
                if (code >= 200 && code < 300) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    mainHandler.post(() -> callback.onSuccess(jsonResponse));
                } else {
                    mainHandler.post(() -> callback.onError("Error code: " + code));
                }
                conn.disconnect();
            } catch (Exception e) {
                Log.e("NetworkHelper", "Post failed", e);
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }
}
