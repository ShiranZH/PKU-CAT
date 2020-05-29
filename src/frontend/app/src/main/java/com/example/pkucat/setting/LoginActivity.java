package com.example.pkucat.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pkucat.App;
import com.example.pkucat.MainActivity;
import com.example.pkucat.R;
import com.example.pkucat.net.*;
import com.example.pkucat.net.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class LoginActivity extends Activity {
    private App app;
    private EditText pkumail;
    private EditText pw;
    private Button register;
    private Button login;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = (App)getApplication();
        pkumail = findViewById(R.id.email);
        pw = findViewById(R.id.passward1);
        message = findViewById(R.id.textView7);
        // register
        register = findViewById(R.id.button6);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tostart = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(tostart);
            }
        });

        // login
        login = findViewById(R.id.button5);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject request = new JSONObject();
                String email = pkumail.getText().toString();
                String password = pw.getText().toString();
                Client client = new Client("https", "49.235.56.155", "443");
                System.out.println(client.user.isLogin());
                try {
                    UserProfile profile = client.user.login("pkuzhd", "123456");
                    System.out.println("登录成功");
                    System.out.println(profile.username);
                    System.out.println(profile.userID);
                    System.out.println(profile.email);
                    System.out.println(profile.whatsup);
                    System.out.println(profile.getAvatar());
                    System.out.println(client.user.isLogin());
                } catch (APIException e) {
                    System.out.println(e.getCode());
                    System.out.println(e.getDescription());
                }
                /*try {
                    request.put("email", email);
                    request.put("password", password);
                    JSONObject response = Client.post(request, new URL("https", app.serverIP, "/user/login"), null);
                    if(!response.getString("code").equals("200")){
                        JSONObject data = response.getJSONObject("data");
                        message.setText(data.get("msg").toString());
                        return;
                    }
                    JSONObject data = response.getJSONObject("data");
                    JSONObject userprofile = data.getJSONObject("profile");
                    app.login_as_user(userprofile);
                    app.cookie = response.getString("cookie");
                } catch (Exception e) {
                    message.setText("未知错误");
                    e.printStackTrace();
                    return;
                }*/
                Intent tostart = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(tostart);
            }
        });
    }
}