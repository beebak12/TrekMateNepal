package com.example.trekmatenepal.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.adapters.PostAdapter;
import com.example.trekmatenepal.data.PostRepository;

public class TrekPostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trek_posts);

        setupHeader();
        setupRecyclerView();
    }

    private void setupHeader() {
        View header = findViewById(R.id.headerLayout);
        TextView title = header.findViewById(R.id.txtHeaderTitle);
        title.setText("Recent Posts");
        header.findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        RecyclerView recycler = findViewById(R.id.recyclerPosts);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new PostAdapter(PostRepository.getAllPosts()));
    }
}
