package com.example.pkucat;

import android.app.Application;

public class App extends Application {
    private boolean is_guest;
    private String username;
    private String mail;
    private int permission;
    private String photoUrl;
    public final String serverIP = "49.235.56.155";

    @Override
    public void onCreate(){
        super.onCreate();
        logout();
    }

    public void login_as_user(String username, String pkumail, String url){
        is_guest = false;
        permission = 1;
        mail = pkumail;
        photoUrl = url;
        this.username = username;
    }

    public void login_as_admin(String username, String pkumail, String url){
        is_guest = false;
        permission = 2;
        mail = pkumail;
        photoUrl = url;
        this.username = username;
    }

    public void logout(){
        is_guest = true;
        username = null;
        mail = "";
        photoUrl = "";
        permission = 0;
    }

    public boolean isguest(){
        return is_guest;
    }

    public String getUsername(){
        return username;
    }

    public int getPermission(){
        return permission;
    }

    public String getMail(){
        return mail;
    }

    public String getPhotoUrl(){
        return photoUrl;
    }
}
