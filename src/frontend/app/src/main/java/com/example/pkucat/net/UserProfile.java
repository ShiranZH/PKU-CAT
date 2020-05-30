package com.example.pkucat.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserProfile {
    public String username;
    public String userID;
    public String email;
    public String whatsup;
    public boolean isAdmin;
    public String[] feedCatID;
    
    private Session session;
    private byte[] avatar;
    private String avatarUrl;
    
    UserProfile(String username, String userID, 
            String email, String whatsup, String avatarUrl,
            boolean isAdmin, String[] feedCatID,
            Session session) {
        this.username = username;
        this.userID = userID;
        this.email = email;
        this.whatsup = whatsup;
        this.avatarUrl = avatarUrl;
        this.session = session;
        this.isAdmin = isAdmin;
        this.feedCatID = feedCatID;
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
        this.isAdmin = profile.getBoolean("is_admin");
        if (profile.has("feed")) {
            JSONArray catArray = profile.getJSONArray("feed");
            this.feedCatID = new String[catArray.length()];
            for (int i = 0; i < catArray.length(); ++i) {
                this.feedCatID[i] = String.valueOf(catArray.getInt(i));
            }
        } else {
            this.feedCatID = new String[0];
        }
        this.session = session;
        this.avatar = null;
    }
    
    public byte[] getAvatar() {
        if (avatar != null)
            return avatar;
        return null;
    }
}
