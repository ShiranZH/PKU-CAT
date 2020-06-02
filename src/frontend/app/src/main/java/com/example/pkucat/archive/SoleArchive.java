package com.example.pkucat.archive;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pkucat.R;

public class SoleArchive extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String catname = bundle.getString("name");
        String info = bundle.getString("info");
        byte catPhoto[] = bundle.getByteArray("photo");
        setContentView(R.layout.activity_sole_archive);
        //bSearchArchive = (Button) findViewById(R.id.searchArchive_button);

        // 设置名称
        setTitle("Cat Archive");

        // 返回键
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView title = findViewById(R.id.sole_archive_title);
        TextView content = findViewById(R.id.sole_archive_content);
        ImageView mImage = findViewById(R.id.sole_archive_pic);

        title.setText(catname);
        content.setText(info);
        Glide.with(SoleArchive.this).load(catPhoto).into(mImage);
//        switch (catId) {
//            case 0:
//                title.setText("山岚");
//                Glide.with(SoleArchive.this).load(R.drawable.catexp0).into(mImage);
//                break;
//            case 1:
//                title.setText("李美人");
//                Glide.with(SoleArchive.this).load(R.drawable.catexp1).into(mImage);
//                break;
//            case 2:
//                title.setText("小芝麻");
//                Glide.with(SoleArchive.this).load(R.drawable.catexp2).into(mImage);
//                break;//
//        }
        //title.setText("aaa");
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
