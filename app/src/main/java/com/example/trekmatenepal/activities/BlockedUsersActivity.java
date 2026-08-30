package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trekmatenepal.R;

public class BlockedUsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_users);

        setupHeader();
    }

    private void setupHeader() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        TextView title = findViewById(R.id.txtHeaderTitle);
        if (title != null) {
            title.setText("Blocked Users");
        }
    }
}
