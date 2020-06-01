package com.example.pkucat.net;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class Cat {
    public String name;
    public String catId;
    private byte[] avatar;
    private String avatarUrl;
    public String info;
    public HashMap<String, String> relations;

    private Session session;
    
    Cat(String name, String avatarUrl, String info,
            HashMap<String, String> relations,
            Session session) {
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.info = info;
        this.relations = relations;
        this.session = session;
        this.avatar = null;
    }
    
    public byte[] getAvatar() {
        if (avatar == null)
            avatar = session.get(session.baseUrl+avatarUrl, null);
        return avatar;
    }
}