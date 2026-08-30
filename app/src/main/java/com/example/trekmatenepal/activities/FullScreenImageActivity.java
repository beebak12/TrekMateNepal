package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;

/**
 * FullScreenImageActivity — shows a single gear image full screen on a black
 * background with a back arrow. Pass the drawable resource id via EXTRA_IMAGE_RES.
 */
public class FullScreenImageActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_RES = "imageResId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        int imageRes = getIntent().getIntExtra(EXTRA_IMAGE_RES, R.drawable.jacket);
        if (imageRes == 0) imageRes = R.drawable.jacket;

        ImageView imgFullScreen = findViewById(R.id.imgFullScreen);
        imgFullScreen.setImageResource(imageRes);

        ImageView btnBack = findViewById(R.id.btnBackFull);
        btnBack.setOnClickListener(v -> finish());
    }
}
