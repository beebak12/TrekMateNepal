package com.example.trekmatenepal.model;

public class FavoriteRequest {
    private final String entity_type;
    private final int entity_id;
    public FavoriteRequest(String entityType, int entityId) {
        this.entity_type = entityType;
        this.entity_id = entityId;
    }
}
