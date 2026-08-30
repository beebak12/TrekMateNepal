package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;

public class MyTrekPostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_list);

        setupHeader();
    }

    private void setupHeader() {
        View header = findViewById(R.id.headerLayout);
        TextView title = header.findViewById(R.id.txtHeaderTitle);
        title.setText("My Trek Posts");
        header.findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}
