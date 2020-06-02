package com.example.pkucat.archive;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pkucat.App;
import com.example.pkucat.R;
import com.example.pkucat.net.APIException;
import com.example.pkucat.net.Cat;
import com.example.pkucat.net.Client;

import org.json.JSONException;

import java.util.HashMap;

public class SoleArchive extends AppCompatActivity {
    private ListView archiveList;
    public HashMap<String, Cat> archiveCats;
    public Cat relatedcatlist[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String catId = String.valueOf(bundle.getInt("catId"));
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

        App app=(App)getApplication();
        Client client=app.client;
        try {
            archiveCats = client.archive.getArchives();
        } catch (APIException e) {
            e.printStackTrace();
        }
        Cat cat = archiveCats.get(catId);

        archiveList = (ListView) findViewById(R.id.sole_archive_relation_list);
        HashMap<String, String> relationCatIds=null;
        try {
            relationCatIds=cat.getRelations();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RelatedCat relatedcats[];
        relatedcats = new RelatedCat[relationCatIds.size()];
        relatedcatlist = new Cat[relationCatIds.size()];
        int i=0;
        for (String key : relationCatIds.keySet()) {
            Cat currentCat = archiveCats.get(key);
            relatedcats[i] = new RelatedCat(relationCatIds.get(key), currentCat.name,currentCat.getAvatar());
            relatedcatlist[i] = currentCat;
            i=i+1;
        }
        archiveList.setAdapter(new RelationList(SoleArchive.this, relatedcats,relationCatIds.size() ));
        archiveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                intent = new Intent(SoleArchive.this, SoleArchive.class);
                Bundle bundle = new Bundle();
                bundle.putInt("catId", position);
                Cat cat=relatedcatlist[position];
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
