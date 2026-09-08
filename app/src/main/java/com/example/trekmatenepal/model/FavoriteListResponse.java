package com.example.trekmatenepal.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FavoriteListResponse {
    private boolean success;
    private List<FavoriteItem> data;
    public boolean isSuccess() { return success; }
    public List<FavoriteItem> getData() { return data; }

    public static class FavoriteItem {
        private int id;
        @SerializedName("entity_type") private String entityType;
        @SerializedName("entity_id") private int entityId;
        public String getEntityType() { return entityType; }
        public int getEntityId() { return entityId; }
    }
}
