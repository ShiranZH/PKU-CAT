package com.example.pkucat.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pkucat.App;
import com.example.pkucat.R;

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
                try {
                    request.put("email", email);
                    request.put("password", password);
                    JSONObject response = Client.post(request, new URL("https", app.serverIP, "/user/login"));
                    if(!response.get("code").equals("200")){
                        JSONObject data = new JSONObject(response.get("data").toString());
                        message.setText(data.get("msg").toString());
                        return;
                    }
                    JSONObject data = response.getJSONObject("data");
                    JSONObject userprofile = response.getJSONObject("profile");
                    JSONObject user = userprofile.getJSONObject("user");
                    app.login_as_user(
                            user.getString("name"), userprofile.getString("mail"),
                            userprofile.getString("avatar"));
                } catch (Exception e) {
                    message.setText("未知错误");
                    return;
                }
                Intent tostart = new Intent(LoginActivity.this, SettingFragment.class);
                startActivity(tostart);
            }
        });
    }
}