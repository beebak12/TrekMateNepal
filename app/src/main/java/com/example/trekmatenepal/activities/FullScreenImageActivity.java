package com.example.trekmatenepal.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.data.RemoteImageLoader;

/**
 * FullScreenImageActivity — shows a single gear image full screen on a black
 * background with a back arrow. Pass the drawable resource id via EXTRA_IMAGE_RES.
 */
public class FullScreenImageActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_RES = "imageResId";
    public static final String EXTRA_IMAGE_URI = "imageUri";
    public static final String EXTRA_IMAGE_URL = "imageUrl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView imgFullScreen = findViewById(R.id.imgFullScreen);
        String imageUri = getIntent().getStringExtra(EXTRA_IMAGE_URI);
        boolean loaded = false;
        if (imageUri != null && !imageUri.trim().isEmpty()) {
            try {
                imgFullScreen.setImageURI(Uri.parse(imageUri));
                loaded = imgFullScreen.getDrawable() != null;
            } catch (RuntimeException error) {
                Log.w("FullScreenImage", "Unable to read uploaded gear image", error);
            }
        }
        if (!loaded) {
            String imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                RemoteImageLoader.load(imgFullScreen, imageUrl, R.drawable.jacket);
            } else {
                int imageRes = getIntent().getIntExtra(EXTRA_IMAGE_RES, R.drawable.jacket);
                imgFullScreen.setImageResource(imageRes > 0 ? imageRes : R.drawable.jacket);
            }
        }

        ImageView btnBack = findViewById(R.id.btnBackFull);
        btnBack.setOnClickListener(v -> finish());
    }
}
