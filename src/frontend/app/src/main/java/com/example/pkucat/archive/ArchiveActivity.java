package com.example.pkucat.archive;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.pkucat.App;
import com.example.pkucat.R;
import com.example.pkucat.net.APIException;
import com.example.pkucat.net.Cat;
import com.example.pkucat.net.Client;
import com.example.pkucat.post.PostActivity;
import com.example.pkucat.post.edit.PostEditActivity;
import com.example.pkucat.post.list.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;


public class ArchiveActivity extends AppCompatActivity {
    private Button bSearchArchive;
    private ListView archiveList;
    private EditText editText;
    public HashMap<String, Cat> archiveCats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_archive);
        setTitle("Cat Archives");

        // 返回键
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
//        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
//        ViewPager viewPager = findViewById(R.id.view_pager);
//        viewPager.setAdapter(sectionsPagerAdapter);
//        TabLayout tabs = findViewById(R.id.tabs_archive);
//        tabs.setupWithViewPager(viewPager);


        App app=(App)getApplication();
        Client client=app.client;
        try {
            archiveCats = client.archive.getArchives();
        } catch (APIException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        bSearchArchive = (Button) findViewById(R.id.searchArchive_button);
        editText=(EditText) findViewById(R.id.search_text);
        setListeners();
        archiveList = (ListView) findViewById(R.id.archive_list);
        archiveList.setAdapter(new MyArchiveList(ArchiveActivity.this, archiveCats));
        archiveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent;
                intent = new Intent(ArchiveActivity.this, SoleArchive.class);
                Bundle bundle = new Bundle();
                bundle.putInt("catId", position);
                Cat cat=archiveCats.get(String.valueOf(position));
                bundle.putInt("catId", Integer.parseInt(cat.catId));
                bundle.putString("name",cat.name);
                try {
                    bundle.putString("info",cat.getInfo());
                } catch (APIException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bundle.putByteArray("photo", cat.getAvatar());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        try {
            if (client.user.getProfile()!=null) {
                try {
                    if (client.user.getProfile().isAdmin) {
                        ImageView icon = new ImageView(this);
                        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_post_text));
                        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                                .setContentView(icon)
                                .build();
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(ArchiveActivity.this, ArchiveAdminitrationActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                } catch (APIException e) {
                    e.printStackTrace();
                }
            }
        } catch (APIException e) {
            e.printStackTrace();
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        bSearchArchive.setOnClickListener((View.OnClickListener) onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ArchiveActivity.this, SoleArchive.class);
            Bundle bundle = new Bundle();
            String catname = editText.getText().toString();
            for (int i= 1; i<=archiveCats.size();i++) {
                Cat cat=archiveCats.get(String.valueOf(i));
                if (cat.name.equals(catname)){
                    bundle.putInt("catId", Integer.parseInt(cat.catId));
                    bundle.putString("name",cat.name);
                    try {
                        bundle.putString("info",cat.getInfo());
                    } catch (APIException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bundle.putByteArray("photo", cat.getAvatar());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return;
                }
            }
            Toast.makeText(ArchiveActivity.this, "查无此猫", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}