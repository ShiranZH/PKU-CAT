package com.example.pkucat.manage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pkucat.R;

public class ManageActivity extends AppCompatActivity {

    private ListView adminUserListView;
    private AdminUserList adminUserList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        // 设置名称
        setTitle("Feeder Management");

        // 返回键
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        adminUserListView = findViewById(R.id.admin_user_list);
        adminUserList = new AdminUserList(this);
        adminUserListView.setAdapter(adminUserList);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.manage_menu, menu);
        return true;
    }

    private int addUser(final String userName, final int level)
    {
        final String level_name = (level==1)?("饲养员"):("管理员");
        int errno = 0;
        String hintMsg = level_name+"设置成功！";
        if(userName.equals("")){hintMsg = "用户名不能为空！";errno = -1;}
        else if (adminUserList.contains(userName)){hintMsg = "该用户已有权限！";errno = -1;}
        else {
            adminUserList.putItem(userName, level);
            adminUserList.notifyDataSetChanged();
        }
        Toast.makeText(ManageActivity.this,hintMsg,Toast.LENGTH_SHORT).show();
        return errno;
    }

    private void addUserWindow(final int level)
    {
        final String level_name = (level==1)?("饲养员"):("管理员");
        final EditText inputName = new EditText(this);
        int errno = 0;
        inputName.setSingleLine(true);
        inputName.setHorizontallyScrolling(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置"+level_name);
        inputName.setHint("请输入用户名");
        builder.setView(inputName);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userName = inputName.getText().toString();
                addUser(userName, level);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.manage_button_add_level1:
                addUserWindow(1);break;
            case R.id.manage_button_add_level2:
                addUserWindow(2);break;
            case R.id.manage_button_sort_by_name:
                adminUserList.sort_by_name();
                adminUserList.notifyDataSetChanged();
                break;
            case R.id.manage_button_sort_by_level:
                adminUserList.sort_by_level();
                adminUserList.notifyDataSetChanged();
                break;

            case android.R.id.home:
                // 返回键
                this.finish(); // back button
                return true;

            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

}
