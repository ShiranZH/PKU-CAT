package com.example.pkucat.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Post {
    public String postID;
    public UserProfile author;
    public Date date;
    public String text;
    public int favorCnt;
    public boolean isFavor;
    public boolean isPublished;
    public Comment[] comments;
    
    private HashMap<String, byte[]> photos;
    
    public void addFavor() {
    }
    
    public HashMap<String, byte[]> getPhotos() {
        return photos;
    }
    
    public void addComment(String text) {
    }
}