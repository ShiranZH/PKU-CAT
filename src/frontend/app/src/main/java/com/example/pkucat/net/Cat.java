package com.example.pkucat.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Cat {
    public String name;
    public String catId;
    private byte[] avatar;
    private String avatarUrl;
    private String info;
    private HashMap<String, String> relations;
    private String[] photoUrls;
    private HashMap<String, byte[]> photos;
    private Session session;

    Cat(JSONObject cat, Session session) throws JSONException  {
        this.name = cat.getString("name");
        this.avatarUrl = cat.getString("avatar");
        this.catId = String.valueOf(cat.getInt("catID"));
        this.session = session;
        this.info = null;
        this.relations = null;
        this.avatar = null;
        this.photoUrls = new String[1];
        this.photos = new HashMap<String, byte[]>();

        this.photoUrls[0] = this.avatarUrl;
        for (int i = 0; i < 1; ++i) {
            this.photos.put(this.photoUrls[i], null);
        }
    }

    public byte[] getAvatar() {
        if (avatar == null)
            avatar = session.get(session.baseUrl+avatarUrl, null);
        return avatar;
    }

    public String getInfo() {
        info = "getInfo";
        return info;
    }

    public HashMap<String, String> getRelations() {
        relations = new HashMap<String, String>();
        relations.put("2", "ÐÖµÜ");
        return relations;
    }

    public HashMap<String, byte[]> getPhotos() {
        for (Map.Entry<String, byte[]> entry : photos.entrySet()) {
            if (entry.getValue() == null) {
                photos.put(entry.getKey(),
                        session.get(session.baseUrl+entry.getKey(), null));
            }
        }
        return photos;
    }
}