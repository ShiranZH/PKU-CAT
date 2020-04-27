package com.example.pkucat.post;

import android.net.Uri;

import java.io.Serializable;

public class PostEntity implements Serializable {
    private String avatarPath;
    private String userName;
    private String postTime;
    private String postContent;
    private String imagePath;

    public PostEntity(String avatarPath, String userName, String postTime, String postContent, String imagePath) {
        this.avatarPath = avatarPath;
        this.userName = userName;
        this.postTime = postTime;
        this.postContent = postContent;
        this.imagePath = imagePath;
    }

    public PostEntity() {
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

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


}
