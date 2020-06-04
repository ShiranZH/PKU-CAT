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
    
    public Post(JSONObject json) throws JSONException, APIException {

        System.out.println(json);
        this.postID = String.valueOf(json.getInt("postID"));
        this.author = User.getProfile(String.valueOf(json.getJSONObject("publisher").getInt("userID")));
        this.date = new Date(json.getLong("time"));
        this.text = json.getString("text");
        this.favorCnt = json.getJSONObject("favor").getInt("favorCnt");
        if (json.getJSONObject("favor").getInt("self") == 1)
            this.isFavor = true;
        else
            this.isFavor = false;
        this.isPublished = true;
        this.comments = null;
    }
    
    public void addFavor() {
    }
    
    public HashMap<String, byte[]> getPhotos() {
        return photos;
    }
    
    public void addComment(String text) {
    }
}