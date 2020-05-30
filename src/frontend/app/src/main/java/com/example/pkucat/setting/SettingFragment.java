package com.example.pkucat.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import android.app.Application;

import com.example.pkucat.App;
import com.example.pkucat.R;

import java.net.URL;
import com.example.pkucat.net.*;
import com.example.pkucat.net.Client;

public class SettingFragment extends Fragment {
    private SettingViewModel settingViewModel;
    private App app;
    private Bitmap bitmap;
    private Handler bmpArrived;
    private Button permissionManage;
    private Button logout_button;
    private TextView username, mail;
    private ImageView avatar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel =
                ViewModelProviders.of(this).get(SettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        app = (App)getActivity().getApplication();
        username = root.findViewById(R.id.textView2);   // username
        mail = root.findViewById(R.id.textView3);   // mail
        avatar = root.findViewById(R.id.imageView2);
        bmpArrived = new Handler(){
            public void handleMessage(Message msg){
                if(msg.what==0x2233) avatar.setImageBitmap(bitmap);
            }
        };


        // 编辑个人信息
        final Button button1 = root.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.isguest()) {
                    Intent tostart = new Intent(getActivity(), LoginActivity.class);
                    startActivity(tostart);
                }
                else{
                    Intent tostart = new Intent(getActivity(), modify.class);
                    startActivity(tostart);
                }
            }
        });

        // 我的关注
        final Button button2 = root.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        // 我的动态
        final Button button3 = root.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        // 退出登录
        logout_button = root.findViewById(R.id.setting_logout);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Client client = app.client;
                    client.user.logout();
                    app.logout();
                    refreshSettingFragment();
                }
                catch (APIException e)
                {

                }
            }
        });

        // 权限管理
        permissionManage = root.findViewById(R.id.button4);
        permissionManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.isguest()) {
                    Intent tostart = new Intent(getActivity(), LoginActivity.class);
                    startActivity(tostart);
                }
                else{
                    Intent tostart = new Intent(getActivity(), modify.class);
                    startActivity(tostart);
                }
            }
        });
        return root;
    }

    private void refreshSettingFragment()
    {
        if(app.getPermission()<2){
            permissionManage.setVisibility(View.INVISIBLE);
        }
        else permissionManage.setVisibility(View.VISIBLE);
        if(!app.isguest()){
            username.setText(app.getUsername());
            mail.setText(app.getMail());
            logout_button.setVisibility(View.VISIBLE);
        }
        else{
            username.setText(R.string.t1);
            mail.setText(R.string.t2);
            avatar.setImageResource(R.drawable.ic_menu_recognition);
            logout_button.setVisibility(View.INVISIBLE);
        }
    }

    public void onResume(){
        super.onResume();
        refreshSettingFragment();
    }

}
