package com.example.trekmatenepal.models;

public class PostModel {

    private String title;
    private String author;
    private String time;
    private String likes;
    private String comments;
    private int image;

    public PostModel(String title, String author, String time, String likes, String comments, int image) {
        this.title = title;
        this.author = author;
        this.time = time;
        this.likes = likes;
        this.comments = comments;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getTime() {
        return time;
    }

    public String getLikes() {
        return likes;
    }

    public String getComments() {
        return comments;
    }

    public int getImage() {
        return image;
    }

}