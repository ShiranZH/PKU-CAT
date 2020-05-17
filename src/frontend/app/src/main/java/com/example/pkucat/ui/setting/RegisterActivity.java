package com.example.pkucat.ui.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.pkucat.App;
import com.example.pkucat.R;

import org.json.JSONObject;

import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    private App app;
    private Button confirm, getvrf;
    private EditText pkumail, pw1, pw2, username;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        app = (App)getApplication();
        confirm = findViewById(R.id.button);
        getvrf = findViewById(R.id.button7);
        pkumail = findViewById(R.id.editText3);
        pw1 = findViewById(R.id.editText4);
        pw2 = findViewById(R.id.editText5);
        username = findViewById(R.id.editText6);
        message = findViewById(R.id.textView14);

        getvrf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = pkumail.getText().toString();
                if(email.equals("")){
                    message.setText("请先输入邮箱");
                    return;
                }
                JSONObject request = new JSONObject();
                try {
                    request.put("email", email);
                    JSONObject response = Client.post(request, new URL("https", app.serverIP, "user/register"));
                    if(!response.getString("code").equals("200")){
                        JSONObject data = response.getJSONObject("data");
                        message.setText(data.getString("msg"));
                    }
                    else{
                        message.setText("验证码已发送至您的邮箱");
                    }
                } catch (Exception e) {
                    message.setText(e.getMessage());
                }
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = pw1.getText().toString(),
                        password2 = pw2.getText().toString();
                if(!password1.equals(password2)){
                    message.setText("两次输入密码不一致");
                    return;
                }
                String email = pkumail.getText().toString();
                String un = username.getText().toString();
                String vrfcode = getvrf.getText().toString();
                JSONObject request = new JSONObject();
                try{
                    request.put("email", email);
                    request.put("username", un);
                    request.put("password", password1);
                    request.put("verificationCode", vrfcode);
                    JSONObject response = Client.post(request, new URL("https", app.serverIP, "user/register/validation"));
                    if(!response.getString("code").equals("200")){
                        JSONObject data = response.getJSONObject("data");
                        message.setText(data.getString("msg"));
                        return;
                    }
                    app.login_as_user(un, email, null);
                }catch (Exception e){
                    message.setText(e.getMessage());
                    return;
                }
                Intent tostart = new Intent(RegisterActivity.this, SettingFragment.class);
                startActivity(tostart);
            }
        });
    }
}
