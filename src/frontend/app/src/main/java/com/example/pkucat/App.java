package com.example.pkucat;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;

import androidx.navigation.ActivityNavigator;

import com.example.pkucat.net.Client;
import com.example.pkucat.net.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

public class App extends Application {
    private boolean is_guest;
    private String username;
    private String mail;
    private int permission;
    private String photoUrl;
    private String whatsup;
    private UserProfile profile;
    public String cookie;
    public Client client = new Client("https", "49.235.56.155", "443");
    public String serverIP = "49.235.56.155";
    private MainActivity mainActivity;

    @Override
    public void onCreate(){
        super.onCreate();
        init();
    }

    public void login(String username, String mail, boolean isAdmin)
    {
        this.username = username;
        this.mail = mail;
        if(isAdmin)permission = 2;
        else permission = 0;
        is_guest = false;
        if(permission == 2)mainActivity.show_Manage();
        mainActivity.fill_user_info();
    }

    public void logout(){
        is_guest = true;
        username = "guest";
        mail = "none";
        photoUrl = "";
        permission = 0;
        mainActivity.hide_Manage();
        mainActivity.fill_user_info();
    }

    public void init(){
        is_guest = true;
        username = "guest";
        mail = "none";
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

    public void setMail(String newMail){mail=newMail;}

    public void setIs_guest(boolean is_guest){this.is_guest = is_guest;}

    public void setPermission(int permission){this.permission = permission;}

    public void setMainActivity(MainActivity mainActivity){this.mainActivity = mainActivity;}
}
