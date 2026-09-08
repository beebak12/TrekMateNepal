package com.example.trekmatenepal.model;

import java.util.List;

public class PartnerPostListResponse {
    private boolean success;
    private List<PartnerPostData> data;
    public boolean isSuccess() { return success; }
    public List<PartnerPostData> getData() { return data; }
    public static class PartnerPostData {
        public int id;
        public int user_id;
        public int required_partners;
        public String travel_date;
        public String expected_duration;
        public String experience_level;
        public String description;
        public String status;
        public String author_name;
        public String group_name;
        public int group_id;
    }
}
