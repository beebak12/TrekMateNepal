package com.example.trekmatenepal.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GearListResponse {
    private boolean success;
    private List<GearData> data;
    private String message;
    public boolean isSuccess() { return success; }
    public List<GearData> getData() { return data; }
    public String getMessage() { return message; }

    public static class GearData {
        private int id;
        private String name, description, availability, size, location, category;
        @SerializedName("price_per_day") private double price;
        @SerializedName("condition_status") private String condition;
        @SerializedName("owner_name") private String ownerName;
        @SerializedName("owner_user_id") private int ownerUserId;
        @SerializedName("image_url") private String imageUrl;
        @SerializedName("owner_profile_image") private String ownerProfileImage;
        public int getId() { return id; }
        public String getName() { return value(name); }
        public String getDescription() { return value(description); }
        public String getAvailability() { return value(availability); }
        public String getSize() { return value(size); }
        public String getLocation() { return value(location); }
        public String getCategory() { return value(category); }
        public double getPrice() { return price; }
        public String getCondition() { return value(condition); }
        public String getOwnerName() { return value(ownerName); }
        public int getOwnerUserId() { return ownerUserId; }
        public String getImageUrl() { return value(imageUrl); }
        public String getOwnerProfileImage() { return value(ownerProfileImage); }
        private String value(String input) { return input == null ? "" : input; }
    }
}
