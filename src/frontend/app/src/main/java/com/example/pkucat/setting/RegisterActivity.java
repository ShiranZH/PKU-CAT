package com.example.pkucat.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.pkucat.App;
import com.example.pkucat.MainActivity;
import com.example.pkucat.R;

import org.json.JSONObject;

import java.net.URL;
import com.example.pkucat.net.*;
import com.example.pkucat.net.Client;

public class RegisterActivity extends AppCompatActivity {
    private App app;
    private Button confirm, getvrf;
    private EditText pkumail, pw1, pw2, username, vrf;
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
        vrf = findViewById(R.id.editText7);
        message = findViewById(R.id.textView14);

        final Client client = app.client;

        getvrf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = pkumail.getText().toString();
                if(email.equals("")){
                    message.setText("请先输入邮箱");
                    return;
                }
                try {
                    client.user.register(email);
                    message.setText("验证码已发送至您的邮箱");
                }
                catch (APIException e)
                {
                    message.setText(e.getDescription());
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
                String vrfcode = vrf.getText().toString();

                try {
                    client.user.registerValidation(email,password1,un,vrfcode);
                    app.login(un,email,false);
                    Intent tostart = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(tostart);
                }
                catch (APIException e)
                {
                    message.setText(e.getDescription());
                }
            }
        });
    }
}
