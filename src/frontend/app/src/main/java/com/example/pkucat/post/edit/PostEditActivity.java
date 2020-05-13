package com.example.pkucat.post.edit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pkucat.R;

import java.io.File;
import java.util.ArrayList;

public class PostEditActivity extends AppCompatActivity {

    private static final int MAX_SELECT_PIC_NUM = 9;
    private Uri imageUri;

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

        ImageView mImageView = findViewById(R.id.post_edit_pic);
        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("ImagePath");
        if (imagePath != null) {
            imageUri = Uri.parse(imagePath);
            mImageView.setImageURI(imageUri);
        }
    }

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

}