package com.example.pkucat.manage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pkucat.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.jar.Attributes;

public class AdminUserList extends BaseAdapter {

    private Context mContext;

    private class UserInfo
    {
        String userName;
        Integer userLevel;
        public UserInfo(){}
        public UserInfo(String _userName, int _userLevel)
        {
            userName = _userName;
            userLevel = _userLevel;
        }
    }
    ArrayList<UserInfo> userInfoList;

    public AdminUserList(Context mContext)
    {
        super();
        this.mContext=mContext;
        userInfoList = new ArrayList<>();
        // example
        userInfoList.add(new UserInfo("Alice",2));
        userInfoList.add(new UserInfo("Bob",1));
        userInfoList.add(new UserInfo("Carol",1));
        userInfoList.add(new UserInfo("David",2));
    }

    @Override
    public int getCount() {
        return userInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return userInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public boolean contains(String name)
    {
        return userInfoList.contains(name);
    }

    public void putItem(String name, int level)
    {
        userInfoList.add(new UserInfo(name, level));
    }

    private class NameComparator implements Comparator<UserInfo>
    {
        public int compare(UserInfo x, UserInfo y)
        {
            return x.userName.compareToIgnoreCase(y.userName);
        }
    }
    private class LevelComparator implements Comparator<UserInfo>
    {
        public int compare(UserInfo x, UserInfo y)
        {
            return x.userLevel.compareTo(y.userLevel);
        }
    }

    public void sort_by_name()
    {
        Collections.sort(userInfoList, new NameComparator());
    }

    public void sort_by_level()
    {
        Collections.sort(userInfoList, new LevelComparator());
    }

    private void deleteUser(final int index)
    {
        final String name = userInfoList.get(index).userName;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("确定要删除用户"+name+"的权限吗？");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userInfoList.remove(index);
                notifyDataSetChanged();
                Toast.makeText(mContext,"删除成功！",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.admin_user_item,null);
        final String name = userInfoList.get(position).userName;
        final int level = userInfoList.get(position).userLevel;
        TextView textViewName = view.findViewById(R.id.admin_name);
        textViewName.setText(name);
        TextView textViewLevel = view.findViewById(R.id.admin_level);
        textViewLevel.setText(level==1?"饲养员":"管理员");
        Button deleteButton = view.findViewById(R.id.admin_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(position);
            }
        });
        return view;
    }
}
