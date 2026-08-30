package com.example.trekmatenepal.models;

public class UserModel {
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private String dob;
    private String age;
    private String gender;
    private String location;
    private String bio;
    private int trekCount;
    private String preferredRegions;
    private String imagePath;

    public UserModel() {}

    public UserModel(String fullName, String username, String email, String phone, String dob, String age, String gender, String location, String bio, int trekCount, String preferredRegions, String imagePath) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.age = age;
        this.gender = gender;
        this.location = location;
        this.bio = bio;
        this.trekCount = trekCount;
        this.preferredRegions = preferredRegions;
        this.imagePath = imagePath;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public int getTrekCount() { return trekCount; }
    public void setTrekCount(int trekCount) { this.trekCount = trekCount; }

    public String getPreferredRegions() { return preferredRegions; }
    public void setPreferredRegions(String preferredRegions) { this.preferredRegions = preferredRegions; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
