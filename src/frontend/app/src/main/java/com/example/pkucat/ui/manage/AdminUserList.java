package com.example.pkucat.ui.manage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.pkucat.R;

import java.util.ArrayList;

public class AdminUserList extends BaseAdapter {

    private Context mContext;
    ArrayList<String> userName;
    ArrayList<Integer> userLevel;

    public AdminUserList(Context mContext)
    {
        super();
        this.mContext=mContext;
        userName = new ArrayList<>();userLevel = new ArrayList<>();
        userName.add("Alice");userLevel.add(2);
        userName.add("Bob");userLevel.add(1);
        userName.add("Carol");userLevel.add(1);
        userName.add("David");userLevel.add(2);
    }

    @Override
    public int getCount() {
        return userName.size();
    }

    @Override
    public Object getItem(int position) {
        return userName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.admin_user_item,null);
        final String name = userName.get(position);
        TextView textViewName = view.findViewById(R.id.admin_name);
        textViewName.setText(userName.get(position));
        TextView textViewLevel = view.findViewById(R.id.admin_level);
        textViewLevel.setText(userLevel.get(position)==1?"饲养员":"管理员");
        Button deleteButton = view.findViewById(R.id.admin_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("提示");
                builder.setMessage("确定要删除用户"+name+"的权限吗？");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
        return view;
    }
}
