package com.example.pkucat.net;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfile {
    public String username;
    public String userID;
    public String email;
    public String whatsup;
    
    private Session session;
    private byte[] avatar;
    private String avatarUrl;
    
    UserProfile(String username, String userID, 
            String email, String whatsup, String avatarUrl,
            Session session) {
        this.username = username;
        this.userID = userID;
        this.email = email;
        this.whatsup = whatsup;
        this.avatarUrl = avatarUrl;
        this.session = session;
        this.avatar = null;
    }
    
    UserProfile(JSONObject profile, Session session) throws JSONException {
        this.username = profile.getJSONObject("user").getString("name");
        this.userID = String.valueOf(profile.getJSONObject("user").getInt("userID"));
        this.email = profile.getString("email");
        if (profile.has("whatsup"))
            this.whatsup = profile.getString("whatsup");
        else
            this.whatsup = "";
        this.avatarUrl = profile.getString("avatar");
        this.session = session;
        this.avatar = null;
    }
    
    public byte[] getAvatar() {
        if (avatar != null)
            return avatar;
        return null;
    }
}
