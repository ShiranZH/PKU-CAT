package com.example.pkucat.post;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class PostEntity implements Serializable {
    private String avatarPath;
    private String userName;
    private String postTime;
    private String postContent;
    private ArrayList<String> imagePath;

    public PostEntity(String avatarPath, String userName, String postTime, String postContent, ArrayList<String> imagePath) {
        this.avatarPath = avatarPath;
        this.userName = userName;
        this.postTime = postTime;
        this.postContent = postContent;
        this.imagePath = imagePath;
    }

    public PostEntity() {
        this.avatarPath = "";
        this.userName = "";
        this.postTime = "";
        this.postContent = "";
        this.imagePath = new ArrayList<>();
    }

    public Uri getAvatarPath() {
        Uri avatarPathUri = Uri.parse(avatarPath);
        return avatarPathUri;
    }
    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostTime() {
        return postTime;
    }
    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostContent() {
        return postContent;
    }
    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public ArrayList<String> getImagePath() {
        return imagePath;
    }
    public int getImageNum() {return imagePath.size();}
    public void setImagePath(ArrayList<String> imagePath) {
        this.imagePath = imagePath;
    }


}
