package com.example.pkucat.ui.manage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pkucat.R;

public class ManageActivity extends AppCompatActivity {

    private ListView adminUserList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        adminUserList = findViewById(R.id.admin_user_list);
        adminUserList.setAdapter(new AdminUserList(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.manage_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        EditText inputName = new EditText(this);
        inputName.setSingleLine(true);
        inputName.setHorizontallyScrolling(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (item.getItemId())
        {
            case R.id.manage_button_add_level1:
                builder.setTitle("设置饲养员");
                inputName.setHint("请输入用户名");
                builder.setView(inputName);
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ManageActivity.this,"饲养员设置成功！",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                break;
            case R.id.manage_button_add_level2:
                builder.setTitle("设置管理员");
                inputName.setHint("请输入用户名");
                builder.setView(inputName);
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ManageActivity.this,"管理员设置成功！",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
}
