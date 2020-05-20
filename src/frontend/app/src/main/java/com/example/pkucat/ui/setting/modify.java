package com.example.pkucat.ui.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pkucat.App;
import com.example.pkucat.MainActivity;
import com.example.pkucat.R;

import org.json.JSONObject;

import java.net.URL;

public class modify extends AppCompatActivity {
    private ImageView avatar;
    private TextView username, whatsup;
    private Button logout;
    private App app;
    private Bitmap bitmap;
    private Handler bmpArrived;
    private LinearLayout chUsername, chwhatsup, chAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        username = findViewById(R.id.textView8);
        whatsup = findViewById(R.id.textView9);
        avatar = findViewById(R.id.imageView5);
        logout = findViewById(R.id.button10);
        app = (App)getApplication();
        chUsername = findViewById(R.id.linearLayout5);
        chwhatsup = findViewById(R.id.linearLayout6);
        chAvatar = findViewById(R.id.linearLayout4);

        chUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject request = new JSONObject();
                try{
                    request.put("username", "something");
                    JSONObject response = Client.put(request, new URL("https", app.serverIP, "/user/profile"), app.cookie);
                    if(response.getString("code").equals("200")){
                        app.setUsername("something");
                        username.setText(app.getUsername());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        chAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        chwhatsup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject request = new JSONObject();
                try{
                    request.put("whatsup", "something");
                    JSONObject response = Client.put(request, new URL("https", app.serverIP, "/user/profile"), app.cookie);
                    if(response.getString("code").equals("200")){
                        app.setWhatsup("something");
                        whatsup.setText(app.getWhatsup());
                    }
                    else System.out.println(response.getJSONObject("data").getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject request = new JSONObject();
                try {
                    JSONObject response = Client.post(request, new URL("https", app.serverIP, "/user/logout"), app.cookie);
                    if(response.getString("code").equals("200")) {
                        app.logout();
                        Intent tostart = new Intent(modify.this, MainActivity.class);
                        startActivity(tostart);
                    }
                    else System.out.println(response.getJSONObject("data").getString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void onResume(){
        super.onResume();

        username.setText(app.getUsername());
        whatsup.setText(app.getWhatsup());
        bmpArrived = new Handler(){
            public void handleMessage(Message msg){
                if(msg.what==0x2233) avatar.setImageBitmap(bitmap);
            }
        };
        new Thread(){
            public void run(){
                try {
                    bitmap = Client.getBmp(new URL("https", app.serverIP, app.getPhotoUrl()));
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(bitmap != null) bmpArrived.sendEmptyMessage(0x2233);
            }
        }.start();
    }
}
