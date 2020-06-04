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


public class ArchiveAdminitrationActivity extends AppCompatActivity {
    private Button bSearchArchive;
    private ListView archiveList;
    private EditText editText;
    public HashMap<String, Cat> archiveCats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_archive_adminitration);
        setTitle("Cat Administration");

        // 返回键
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }


        App app=(App)getApplication();
        Client client=app.client;
        try {
            archiveCats = client.archive.getArchives();
        } catch (APIException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);


        HashMap<String,Cat> adminitratedCats=archiveCats;
        final Cat adCatList[]=new Cat[adminitratedCats.size()];
        String catNames[]=new String[adminitratedCats.size()];
        int size=0;
        for (String key:adminitratedCats.keySet()){
            adCatList[size] = adminitratedCats.get(key);
            catNames[size] = adminitratedCats.get(key).name;
            size++;
        }

        archiveList = (ListView) findViewById(R.id.archive_list);
        archiveList.setAdapter(new AdministrationList(ArchiveAdminitrationActivity.this, catNames, size));
        archiveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                intent = new Intent(ArchiveAdminitrationActivity.this, EditArchive.class);
                Bundle bundle = new Bundle();
                Cat cat=adCatList[position];
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
                try {
                    adCatList[position].refresh();
                } catch (APIException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

}