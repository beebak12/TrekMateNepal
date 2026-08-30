package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.data.NotificationRepository;
import com.example.trekmatenepal.data.PostRepository;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.models.JoinRequestModel;
import com.example.trekmatenepal.models.PostModel;

public class PostDetailActivity extends AppCompatActivity {

    private PostModel post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        post = (PostModel) getIntent().getSerializableExtra("post");
        if (post == null) {
            finish();
            return;
        }

        initViews();
    }

    private void initViews() {
        ImageView imgTrek = findViewById(R.id.imgTrek);
        TextView txtTitle = findViewById(R.id.txtTitle);
        TextView txtLocation = findViewById(R.id.txtLocation);
        TextView txtAuthorName = findViewById(R.id.txtAuthorName);
        TextView txtDates = findViewById(R.id.txtDates);
        TextView txtBudget = findViewById(R.id.txtBudget);
        TextView txtExperience = findViewById(R.id.txtExperience);
        TextView txtSlots = findViewById(R.id.txtSlots);
        TextView txtDescription = findViewById(R.id.txtDescription);
        Button btnJoin = findViewById(R.id.btnJoin);
        Button btnMessageAdmin = findViewById(R.id.btnMessageAdmin);

        // Populate Data
        if (post.getCustomImageUri() != null) {
            imgTrek.setImageURI(Uri.parse(post.getCustomImageUri()));
        } else {
            imgTrek.setImageResource(post.getImageRes() != 0 ? post.getImageRes() : R.drawable.everest);
        }

        txtTitle.setText(post.getTitle());
        txtLocation.setText(post.getLocation());
        txtAuthorName.setText(post.getAuthor());
        txtDates.setText(post.getDateRange());
        txtBudget.setText(post.getBudget() != null && !post.getBudget().isEmpty() ? post.getBudget() : "Flexible");
        txtExperience.setText(post.getExperienceLevel() != null && !post.getExperienceLevel().isEmpty() ? post.getExperienceLevel() : "Any");
        txtSlots.setText(post.getInterestedCount() + " spots");
        txtDescription.setText(post.getDescription() != null && !post.getDescription().isEmpty() ? post.getDescription() : "No additional details provided.");

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btnJoin.setOnClickListener(v -> handleJoinRequest());
        
        btnMessageAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("partnerName", post.getAuthor());
            intent.putExtra("partnerImage", post.getImageRes());
            startActivity(intent);
        });
    }

    private void handleJoinRequest() {
        String currentUserId = SessionUser.getUserId(this);
        if (post.getAuthorId() != null && post.getAuthorId().equals(currentUserId)) {
            Toast.makeText(this, "You are the admin of this trek", Toast.LENGTH_SHORT).show();
            return;
        }

        JoinRequestModel request = new JoinRequestModel(post.getId(), currentUserId, "You");
        PostRepository.addJoinRequest(this, request);

        // Notify Admin
        NotificationRepository.notifyJoinRequest(this, post.getAuthorId(), "Someone", post.getTitle());

        Toast.makeText(this, "Request sent to " + post.getAuthor(), Toast.LENGTH_SHORT).show();
        finish();
    }
}
