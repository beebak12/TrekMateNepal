package com.example.trekmatenepal.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.text.TextUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trekmatenepal.R;
import com.example.trekmatenepal.data.GearBackendRepository;
import com.example.trekmatenepal.data.RemoteImageLoader;
import com.example.trekmatenepal.data.NotificationRepository;
import com.example.trekmatenepal.data.SessionUser;
import com.example.trekmatenepal.models.RentalGearModel;

/**
 * PostGearActivity — lets a user list their gear for rental.
 */
public class PostGearActivity extends AppCompatActivity {

    private static final String TAG = "PostGearActivity";
    public static final String EXTRA_EDIT_GEAR = "edit_gear";

    private EditText etName, etCategory, etSize, etPrice, etDescription, etLocation;
    private FrameLayout layoutSelectImage;
    private ImageView imgPreview;
    private LinearLayout layoutAddIcon;
    private Button btnPost;
    private View layoutSizeSection;
    private TextView tvFormTitle;

    private Uri selectedImageUri;
    private RentalGearModel editingGear;

    private final ActivityResultLauncher<String[]> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
                if (uri != null) {
                    try {
                        getContentResolver().takePersistableUriPermission(
                                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        selectedImageUri = uri;
                    } catch (SecurityException error) {
                        Log.w(TAG, "Image provider did not offer persistent read access", error);
                        selectedImageUri = null;
                        Toast.makeText(this,
                                "This image cannot be kept after the app closes. Choose another image.",
                                Toast.LENGTH_LONG).show();
                    }
                    imgPreview.setImageURI(uri);
                    imgPreview.setVisibility(View.VISIBLE);
                    layoutAddIcon.setVisibility(View.GONE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_gear);

        initViews();
        loadEditingGear();
        setupListeners();
    }

    private void initViews() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        etName = findViewById(R.id.etGearName);
        etCategory = findViewById(R.id.etCategory);
        etSize = findViewById(R.id.etSize);
        layoutSizeSection = findViewById(R.id.layoutSizeSection);
        tvFormTitle = findViewById(R.id.tvFormTitle);
        etPrice = findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDescription);
        etLocation = findViewById(R.id.etLocation);
        
        layoutSelectImage = findViewById(R.id.layoutSelectImage);
        imgPreview = findViewById(R.id.imgPreview);
        layoutAddIcon = findViewById(R.id.layoutAddIcon);
        btnPost = findViewById(R.id.btnPostGear);
    }

    private void setupListeners() {
        layoutSelectImage.setOnClickListener(v -> pickImageLauncher.launch(new String[]{"image/*"}));

        btnPost.setOnClickListener(v -> handlePostGear());
        etCategory.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { updateSizeField(); }
            @Override public void afterTextChanged(Editable s) { }
        });
    }

    private void loadEditingGear() {
        editingGear = (RentalGearModel) getIntent().getSerializableExtra(EXTRA_EDIT_GEAR);
        if (editingGear == null) {
            updateSizeField();
            return;
        }
        tvFormTitle.setText("Edit Gear");
        btnPost.setText("Update Gear");
        etName.setText(editingGear.getName());
        etCategory.setText(editingGear.getCategory());
        etSize.setText(editingGear.getSize());
        etPrice.setText(editingGear.getPriceRaw());
        etDescription.setText(editingGear.getDescription());
        etLocation.setText(editingGear.getLocation());
        if (!editingGear.getCustomImageUri().isEmpty()) {
            try {
                selectedImageUri = Uri.parse(editingGear.getCustomImageUri());
                imgPreview.setImageURI(selectedImageUri);
                imgPreview.setVisibility(View.VISIBLE);
                layoutAddIcon.setVisibility(View.GONE);
            } catch (RuntimeException error) {
                Log.w(TAG, "Unable to preview the saved gear image", error);
            }
        } else if (editingGear.getImage() > 0) {
            imgPreview.setImageResource(editingGear.getImage());
            imgPreview.setVisibility(View.VISIBLE);
            layoutAddIcon.setVisibility(View.GONE);
        } else if (!editingGear.getRemoteImageUrl().isEmpty()) {
            RemoteImageLoader.load(imgPreview, editingGear.getRemoteImageUrl(), R.drawable.jacket);
            imgPreview.setVisibility(View.VISIBLE);
            layoutAddIcon.setVisibility(View.GONE);
        }
        updateSizeField();
    }

    private void updateSizeField() {
        if (layoutSizeSection == null || etCategory == null) return;
        String category = text(etCategory);
        boolean footwear = category.equalsIgnoreCase("Footwear");
        boolean clothing = category.equalsIgnoreCase("Clothing");
        layoutSizeSection.setVisibility(footwear || clothing ? View.VISIBLE : View.GONE);
        if (footwear) {
            etSize.setHint("e.g. 42");
            etSize.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (clothing) {
            etSize.setHint("M / L / XL / XXL");
            etSize.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        }
    }

    private void handlePostGear() {
        String name = text(etName);
        String category = text(etCategory);
        String size = text(etSize);
        String priceNum = text(etPrice);
        String description = text(etDescription);
        String location = text(etLocation);

        if (TextUtils.isEmpty(name)) { etName.setError("Required"); return; }
        if (TextUtils.isEmpty(category)) { etCategory.setError("Required"); return; }
        if (TextUtils.isEmpty(priceNum)) { etPrice.setError("Required"); return; }
        if (category.equalsIgnoreCase("Footwear") && !size.matches("\\d{1,3}")) {
            etSize.setError("Enter footwear size as a number, for example 42");
            return;
        }
        if (category.equalsIgnoreCase("Clothing")) {
            size = size.toUpperCase().replaceAll("\\s+", "");
            if (!size.matches("(S|M|L|XL|XXL)(/(S|M|L|XL|XXL))*")) {
                etSize.setError("Use clothing sizes such as M, L, XL or XXL");
                return;
            }
            size = size.replace("/", " / ");
        } else if (!category.equalsIgnoreCase("Footwear")) {
            size = "";
        }

        String currentUserId = SessionUser.getUserId(this);
        String sellerName = editingGear != null
                ? editingGear.getSeller() : SessionUser.getDisplayName(this);
        
        String priceDisplay = "Rs. " + priceNum + " / week";

        RentalGearModel gear = new RentalGearModel(
                0, // No default resource
                selectedImageUri != null ? selectedImageUri.toString()
                        : editingGear != null ? editingGear.getCustomImageUri() : null,
                name,
                category,
                "New",
                priceDisplay,
                priceNum,
                editingGear != null ? editingGear.getAvailability() : "Available",
                location,
                description,
                size,
                editingGear != null ? editingGear.getCondition() : "Good",
                sellerName,
                currentUserId
        );

        if (editingGear != null) gear.setId(editingGear.getId());
        gear.setRemoteImageUrl(editingGear != null ? editingGear.getRemoteImageUrl() : "");
        btnPost.setEnabled(false);
        btnPost.setText(editingGear != null ? "Updating…" : "Posting…");
        GearBackendRepository.save(this, gear, selectedImageUri,
                new GearBackendRepository.ActionCallback() {
                    @Override public void success(RentalGearModel saved) {
                        if (editingGear == null) NotificationRepository.notifyGearListed(
                                PostGearActivity.this, currentUserId, name);
                        Toast.makeText(PostGearActivity.this,
                                editingGear == null ? "Gear posted successfully!" : "Gear updated successfully!",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override public void error(String message) {
                        btnPost.setEnabled(true);
                        btnPost.setText(editingGear != null ? "Update Gear" : "Post Gear");
                        Toast.makeText(PostGearActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private String text(EditText e) {
        return e == null ? "" : e.getText().toString().trim();
    }
}
