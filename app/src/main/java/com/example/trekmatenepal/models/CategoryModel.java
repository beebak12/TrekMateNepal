package com.example.trekmatenepal.models;

public class CategoryModel {

    private int image;
    private String categoryName;

    public CategoryModel(int image, String categoryName) {
        this.image = image;
        this.categoryName = categoryName;
    }

    public int getImage() {
        return image;
    }

    public String getCategoryName() {
        return categoryName;
    }
}