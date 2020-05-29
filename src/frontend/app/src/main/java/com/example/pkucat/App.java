package com.example.pkucat;

import android.app.Application;

import com.example.pkucat.net.Client;

import org.json.JSONException;
import org.json.JSONObject;

public class App extends Application {
    private boolean is_guest;
    private String username;
    private String mail;
    private int permission;
    private String photoUrl;
    private String whatsup;
    public String cookie;
    public Client client ;
    public final String serverIP = "49.235.56.155";

    @Override
    public void onCreate(){
        super.onCreate();
        logout();
    }

    public void login_as_user(JSONObject profile) throws JSONException {
        is_guest = false;
        permission = 1;
        JSONObject user = profile.getJSONObject("user");
        username = user.getString("name");
        mail = profile.getString("mail");
        photoUrl = profile.getString("avatar");
        whatsup = profile.getString("whatsup");
    }

    public void login_as_admin(JSONObject profile) throws JSONException {
        is_guest = false;
        permission = 2;
        JSONObject user = profile.getJSONObject("user");
        username = user.getString("name");
        mail = profile.getString("mail");
        photoUrl = profile.getString("avatar");
        whatsup = profile.getString("whatsup");
    }

    public void logout(){
        is_guest = true;
        username = "";
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

    public String getWhatsup(){
        return whatsup;
    }

    public void setUsername(String newUN){
        username = newUN;
    }

    public void setWhatsup(String newWhatsup){
        whatsup = newWhatsup;
    }

    public void setPhotoUrl(String url){
        photoUrl = url;
    }
}
