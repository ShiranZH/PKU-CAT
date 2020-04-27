package com.example.pkucat.ui.archive;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pkucat.R;

public class SoleArchive extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        int catId = bundle.getInt("catId");
        setContentView(R.layout.activity_sole_archive);
        //bSearchArchive = (Button) findViewById(R.id.searchArchive_button);


        TextView title = findViewById(R.id.sole_archive_title);
        ImageView mImage = findViewById(R.id.sole_archive_pic);


        switch (catId) {
            case 0:
                title.setText("山岚");
                Glide.with(SoleArchive.this).load(R.drawable.catexp0).into(mImage);
                break;
            case 1:
                title.setText("李美人");
                Glide.with(SoleArchive.this).load(R.drawable.catexp1).into(mImage);
                break;
            case 2:
                title.setText("小芝麻");
                Glide.with(SoleArchive.this).load(R.drawable.catexp2).into(mImage);
                break;

        }
        //title.setText("aaa");
    }
}

