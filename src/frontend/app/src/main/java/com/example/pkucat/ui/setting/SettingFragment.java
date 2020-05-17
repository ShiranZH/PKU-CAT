package com.example.pkucat.ui.setting;

import android.content.Intent;
import android.os.Bundle;
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

public class SettingFragment extends Fragment {
    private SettingViewModel settingViewModel;
    private App app;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel =
                ViewModelProviders.of(this).get(SettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        app = (App)getActivity().getApplication();

        final TextView textView2 = root.findViewById(R.id.textView2);   // username
        final TextView textView3 = root.findViewById(R.id.textView2);   // mail
        final ImageView avatar = root.findViewById(R.id.imageView2);
        if(!app.isguest()){
            textView2.setText(app.getUsername());
            textView3.setText(app.getMail());

        }


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
            }
        });

        // 我的动态
        final Button button3 = root.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // 权限管理
        final Button button4 = root.findViewById(R.id.button4);
        if(app.getPermission()<2){
            button4.setVisibility(View.INVISIBLE);
        }
        else {
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        final ImageView iv = root.findViewById(R.id.imageView2);
        iv.setOnClickListener(new View.OnClickListener() {
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

}
