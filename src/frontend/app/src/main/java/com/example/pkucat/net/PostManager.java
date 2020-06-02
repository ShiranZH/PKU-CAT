package com.example.pkucat.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class PostManager {
    private Session session;
    private String baseUrl;
    
    PostManager(Session sess, String baseUrl) {
        this.session = sess;
        this.baseUrl = baseUrl;
    }
    
    public Post addPost(Post post) {
        return null;
    }
    
    public void delPost(String postID) {

    }
    
    public Post[] getPosts(int num, int start, int commentNum) {
        return null;
    }
    
    public Post[] searchPosts(String userID, String catID, String keyword, int commentNum) {
        return null;
    }
}