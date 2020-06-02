package com.example.pkucat.net;

import java.io.File;
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

    Cat(JSONObject cat) throws JSONException  {
        this.name = cat.getString("name");
        this.avatarUrl = cat.getString("avatar");
        this.catId = String.valueOf(cat.getInt("catID"));
        this.info = null;
        this.relations = null;
        this.avatar = null;
        this.photoUrls = null;
        this.photos = null;
    }

    public byte[] getAvatar() {
        if (avatar == null)
            avatar = Session.get(avatarUrl, null);
        return avatar;
    }
    public void refresh() throws APIException, JSONException {
        JSONObject data = new JSONObject();
        try {
            data.put("catid", catId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        byte[] ret = Session.get("/user/archive", data);
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
        JSONArray photoArray = retData.getJSONObject("data").getJSONObject("archive").getJSONArray("photos");
        photoUrls = new String[photoArray.length()];
        photos = new HashMap<String, byte[]>();
        for (int i = 0; i < photoArray.length(); ++i) {
            String url = photoArray.getString(i);
            byte[] photo = Session.get(url, null);
            photos.put(url, photo);
        }
    }
    
    public String getInfo() throws APIException, JSONException {
        if (info == null) {
            refresh();
        }
        return info;
    }
    
    public HashMap<String, String> getRelations() throws APIException, JSONException {
        if (relations == null) {
            refresh();
        }
        return relations;
    }

    public HashMap<String, byte[]> getPhotos() throws JSONException, APIException {
        if (photoUrls == null) {
            refresh();
        }
        return photos;
    }
    
    public void modifyInfo(String _info) throws APIException {
        JSONObject data = new JSONObject();
        data.put("id", this.catId);
        data.put("introduction", _info);

        try {
            byte[] ret = Session.put("/user/archive", data);
            if (ret == null)
                throw new APIException("404", "ÍøÂç´íÎó");
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
            
            info = _info;
        } catch (JSONException e) {
            throw new APIException("404", "·µ»ØÖµ´íÎó");
        } catch (APIException e) {
            throw e;
        }
    }
    
    public void modifyAvatar(File file) throws APIException {
        File[] files = new File[]{file};
        String[] urls = Session.uploadPicture(files);
        JSONObject data = new JSONObject();
        data.put("id", this.catId);
        data.put("avatar", urls[0]);
        
        try {
            byte[] ret = Session.put("/user/archive", data);
            if (ret == null)
                throw new APIException("404", "ÍøÂç´íÎó");
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
            
            avatarUrl = urls[0];
            avatar = Session.get(avatarUrl, null);
            
        } catch (JSONException e) {
            throw new APIException("404", "·µ»ØÖµ´íÎó");
        } catch (APIException e) {
            throw e;
        }
    }
}