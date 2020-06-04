package com.example.pkucat.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment {
    public String commentID;
    public Date date;
    public String text;
    public UserProfile author;
    
    Comment(JSONObject json) throws JSONException, APIException {
        this.commentID = String.valueOf(json.getInt("commentID"));
        this.author = User.getProfile(String.valueOf(json.getJSONObject("user").getInt("userID")));
        this.date = new Date(json.getLong("time"));
        this.text = json.getString("text");
    }

}