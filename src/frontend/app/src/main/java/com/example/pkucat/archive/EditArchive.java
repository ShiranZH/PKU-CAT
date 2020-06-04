package com.example.pkucat.archive;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pkucat.App;
import com.example.pkucat.R;
import com.example.pkucat.net.APIException;
import com.example.pkucat.net.Cat;
import com.example.pkucat.net.Client;

import org.json.JSONException;

import java.util.HashMap;

public class EditArchive extends AppCompatActivity {
    private Button ChangePicButton;
    private Button ChangeIntroButton;
    private EditText editText;
    private static final int CHOOSE_PHOTO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        final String catId = String.valueOf(bundle.getInt("catId"));
        String info = bundle.getString("info");
        byte catPhoto[] = bundle.getByteArray("photo");
        setContentView(R.layout.activity_edit_archive);                // 设置名称
        setTitle("Edit Archive");

        // 返回键
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView content = findViewById(R.id.edit_cat_intro);
        ImageView mImage = findViewById(R.id.sole_archive_pic);

        content.setText(info);
        Glide.with(EditArchive.this).load(catPhoto).into(mImage);

        editText=(EditText) findViewById(R.id.edit_cat_intro);
        ChangePicButton = (Button) findViewById(R.id.change_pic_button);
        ChangeIntroButton = (Button) findViewById(R.id.change_intro_button);
        ChangeIntroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newIntro = editText.getText().toString();
                App app=(App)getApplication();
                Client client=app.client;
                try {
                    HashMap<String,Cat> archiveCats = client.archive.getArchives();
                    Cat cat=archiveCats.get(catId);
                    cat.modifyInfo(newIntro);
                } catch (APIException | JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(EditArchive.this, "新的猫咪简介已保存", Toast.LENGTH_SHORT).show();
            }
        });
        ChangePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void selectPicture() {
        Intent picIntent = new Intent(Intent.ACTION_GET_CONTENT);
        picIntent.setType("image/*");
        startActivityForResult(picIntent, CHOOSE_PHOTO);
    }
}
