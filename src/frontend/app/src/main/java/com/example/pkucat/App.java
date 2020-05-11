package com.example.pkucat;

import android.app.Application;

public class App extends Application {
    private boolean is_guest;
    private String username;
    private String mail;
    private int permission;

    @Override
    public void onCreate(){
        super.onCreate();
        logout();
    }

    public void login_as_user(String username){
        is_guest = false;
        permission = 1;
        this.username = username;
    }

    public void login_as_admin(String username){
        is_guest = false;
        permission = 2;
        this.username = username;
    }

    public void logout(){
        is_guest = true;
        username = null;
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
}
