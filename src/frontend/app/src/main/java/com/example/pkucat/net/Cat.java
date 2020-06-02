package com.example.pkucat.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
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
    
    public void refresh() throws APIException {
        JSONObject data = new JSONObject();
        data.put("catid", catId);
        byte[] ret = session.get(session.baseUrl+"/user/archive", data);
        JSONObject retData = new JSONObject(new String(ret));

        if (retData.getInt("code") != 200)
            throw new APIException(retData);
        JSONObject archive = retData.getJSONObject("data").getJSONObject("archive");
        info = archive.getString("introduction");
        JSONArray relatedCats = archive.getJSONArray("relatedCats");
        relations = new HashMap<String, String>();
        for (int i = 0; i < relatedCats.length(); ++i) {
            relations.put(String.valueOf(relatedCats.getJSONObject(i).getInt("relatedCat")),
                    relatedCats.getJSONObject(i).getString("relation"));
        }
        // TODO: photos
    }
    
    public String getInfo() throws APIException {
        if (info == null) {
            refresh();
        }
        return info;
    }
    
    public HashMap<String, String> getRelations() throws APIException {
        if (relations == null) {
            refresh();
        }
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