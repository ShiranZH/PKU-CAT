package com.example.pkucat.post.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pkucat.R;

import java.util.ArrayList;

public class PostEditActivity extends AppCompatActivity {


    private static final int CHOOSE_PHOTO = 1;
    private static final int TAKE_PHOTO = 2;
    private static final int RECORD_VIDEO = 3;
    private Uri imageUri;
    private String imagePath;
    ArrayList<Uri> uriArrayList = new ArrayList<>();;
    ImageAdapter imageAdapter;
    GridView gridView;

    private static final int MENU_POST = 18; // id of this menu item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_edit);
        // 设置名称
        setTitle("Edit Post");

        // 返回键
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("ImagePath");
        if (imagePath != null) {
            imageUri = Uri.parse(imagePath);
            uriArrayList.add(imageUri);
        }

        imageAdapter = new ImageAdapter(this, uriArrayList);
        gridView = findViewById(R.id.post_edit_pic_group);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ImageView imageView = (ImageView) view.getTag();

                /**代表+号之前的需要正常显示图片**/
                if (position < uriArrayList.size()) {
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 点击操作，后续可添加点击后的响应
                            final AlertDialog.Builder builder = new AlertDialog.Builder(PostEditActivity.this);
                            builder.setItems(new String[]{"删除", "取消"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            uriArrayList.remove(position);
                                            imageAdapter = new ImageAdapter(PostEditActivity.this, uriArrayList);
                                            gridView.setAdapter(imageAdapter);
                                            imageAdapter.notifyDataSetChanged();
                                            Toast.makeText(PostEditActivity.this, position+"删除成功！", Toast.LENGTH_SHORT).show();
                                        default:
                                            break;
                                    }
                                }
                            });
                            builder.show();
                        }
                    });
                } else if (position == uriArrayList.size()){
                    /**代表+号的需要+号图片显示图片**/
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            ((Activity) PostEditActivity.this).startActivityForResult(intent, CHOOSE_PHOTO);
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 返回键
            case android.R.id.home:
                PostEditActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post_edit_menu, menu);
        //添加标题栏的搜索按钮
        MenuItem item=menu.add(0, MENU_POST, 0, "Search");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.ic_post_send);//设置图标

        MenuItem.OnMenuItemClickListener listener=new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case MENU_POST:
                        final EditText inputName = new EditText(PostEditActivity.this);
                        inputName.setSingleLine(true);
                        inputName.setHorizontallyScrolling(true);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(PostEditActivity.this);
                        // Toast.makeText(PostActivity.this, "您点击了搜索按钮", Toast.LENGTH_SHORT).show();
                        builder.setMessage("确定要发布这条动态吗？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO: 传递数据给后端，同时本地缓存，直接显示在返回界面的首位
                                Toast.makeText(PostEditActivity.this,"发布成功！",Toast.LENGTH_SHORT).show();
                                PostEditActivity.this.finish();
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
                uriArrayList.add(imageUri);
                imageAdapter = new ImageAdapter(this, uriArrayList);
                gridView.setAdapter(imageAdapter);
                imageAdapter.notifyDataSetChanged();
                break;
            case TAKE_PHOTO:
                break;
            case RECORD_VIDEO:
                // media player
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageAdapter = new ImageAdapter(this, uriArrayList);
        gridView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();
    }


}