package com.example.pkucat.post;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.pkucat.R;
import com.example.pkucat.manage.ManageActivity;
import com.example.pkucat.post.edit.PostEditActivity;
import com.example.pkucat.post.list.SectionsPagerAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostActivity extends AppCompatActivity {

    private static final int CHOOSE_PHOTO = 1;
    private static final int TAKE_PHOTO = 2;
    private static final int RECORD_VIDEO = 3;
    private String imagePath = "";

    private static final int MENU_SEARCH = 17; // id of this menu item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        // 设置名称
        setTitle("Cat Community");

        // 返回键
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs_post);
        tabs.setupWithViewPager(viewPager);


        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = "https://49.235.56.155/posts/?limit=10&start=10";
                try {
                    try {
                        URL url = new URL(path); //新建url并实例化
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");//获取服务器数据
                        connection.setReadTimeout(8000);//设置读取超时的毫秒数
                        connection.setConnectTimeout(8000);//设置连接超时的毫秒数
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                        System.out.println(result);
                    } catch (MalformedURLException e) {
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 编辑动态的浮标
        ImageView icon = new ImageView(this);
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_post_add));
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();

        SubActionButton textButton = buildSubActionButton(this, R.drawable.ic_post_text);
        SubActionButton photoButton = buildSubActionButton(this, R.drawable.ic_post_photo);
        SubActionButton cameraButton = buildSubActionButton(this, R.drawable.ic_post_camera);
        SubActionButton videoButton = buildSubActionButton(this, R.drawable.ic_post_video);

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(textButton)
                .addSubActionView(photoButton)
                .addSubActionView(cameraButton)
                .addSubActionView(videoButton)
                .attachTo(actionButton)
                .build();

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PostActivity.this, PostEditActivity.class);
                startActivity(intent);
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "To be implemented...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                takePhoto();
            }
        });

        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "To be implemented...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                recordVideo();
            }
        });
    }
    /****** begin of edit post ******/
    /*** select picture ***/
    private void selectPicture() {
        Intent picIntent = new Intent(Intent.ACTION_GET_CONTENT);
        picIntent.setType("image/*");
        startActivityForResult(picIntent, CHOOSE_PHOTO);
    }

    /*** take photo ***/
    private void takePhoto() {
        /*
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, TAKE_PHOTO);
         */
    }

    /*** record video ***/
    private void recordVideo() {
        /*
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(videoIntent, RECORD_VIDEO);
         */
    }

    /****** end of edit post ******/

    /****** begin of edit post from https://www.mscto.com/android/134795.html ******/

    /*** onActivityResult ***/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "You cancelled it.", Toast.LENGTH_LONG).show();
            return;
        } else if (resultCode != RESULT_OK) {
            Toast.makeText(this, "Err... Something went error.", Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode) {
            case CHOOSE_PHOTO:
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    imagePath = imageUri.toString();
                } else {
                    imagePath = "";
                }
                break;
            case TAKE_PHOTO:
                break;
            case RECORD_VIDEO:
                // media player
                break;
            default:
                break;
        }
        Intent intent = new Intent();
        intent.setClass(PostActivity.this, PostEditActivity.class);
        intent.putExtra("ImagePath", imagePath);
        startActivity(intent);
    }
    /****** end of edit post from https://www.mscto.com/android/134795.html ******/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 返回键
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private SubActionButton buildSubActionButton(Activity activity, int iconResourceId) {
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(activity);
        ImageView itemIcon = new ImageView(activity);
        itemIcon.setImageResource(iconResourceId);
        return itemBuilder.setContentView(itemIcon).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post_list_menu, menu);
        //添加标题栏的搜索按钮
        MenuItem item=menu.add(0, MENU_SEARCH, 0, "Search");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.ic_post_search);//设置图标

        MenuItem.OnMenuItemClickListener listener=new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case MENU_SEARCH:
                        final EditText inputName = new EditText(PostActivity.this);
                        inputName.setSingleLine(true);
                        inputName.setHorizontallyScrolling(true);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                        // Toast.makeText(PostActivity.this, "您点击了搜索按钮", Toast.LENGTH_SHORT).show();
                        builder.setTitle("搜索动态");
                        inputName.setHint("请输入用户名");
                        builder.setView(inputName);
                        builder.setPositiveButton("搜索", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(PostActivity.this,"搜索成功！",Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        };
        item.setOnMenuItemClickListener(listener);//添加监听事件
        return true;
    }


}

