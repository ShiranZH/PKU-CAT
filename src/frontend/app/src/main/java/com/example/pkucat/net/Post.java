package com.example.pkucat.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
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
    
    public void addFavor() throws APIException, JSONException {
        if (isFavor == false) {
            JSONObject data = new JSONObject();
            data.put("postID", postID);
            byte[] ret = Session.post("/user/post/favor", data, null);
            
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
        }
    }
    
    public HashMap<String, byte[]> getPhotos() {
        return photos;
    }
    
    public Comment[] getComments(int num, int start) throws APIException {
        try {
            JSONObject data = new JSONObject();
            data.put("postID", String.valueOf(postID));
            data.put("limit", String.valueOf(num));
            data.put("start", String.valueOf(start));
            byte[] ret = Session.get("/user/post/comments", data);

            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
            int commentCnt = retData.getJSONObject("data").getJSONObject("commentList").getInt("downloadCount");
            Comment[] comments = new Comment[commentCnt];
            JSONArray postArray = retData.getJSONObject("data").getJSONObject("commentList").getJSONArray("comments");
            for (int i = 0; i < postArray.length(); ++i) {
                comments[i] = new Comment(postArray.getJSONObject(i));
            }
            
            return comments;
        } catch (JSONException e) {
            throw new APIException("404", "·µ»ØÖµ´íÎó");
        } catch (APIException e) {
            throw e;
        }
    }
    
    public void addComment(String text) throws APIException, JSONException {
        JSONObject data = new JSONObject();
        data.put("text", text);
        data.put("postID", postID);
        byte[] ret = Session.post("/user/post/comment", data, null);
        
        JSONObject retData = new JSONObject(new String(ret));
        if (retData.getInt("code") != 200)
            throw new APIException(retData);
    }
}