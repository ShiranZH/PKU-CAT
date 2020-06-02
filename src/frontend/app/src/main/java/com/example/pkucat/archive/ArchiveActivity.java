package com.example.pkucat.archive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pkucat.App;
import com.example.pkucat.R;
import com.example.pkucat.net.APIException;
import com.example.pkucat.net.Cat;
import com.example.pkucat.net.Client;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class ArchiveActivity extends Activity {
    private Button bSearchArchive;
    private ListView archiveList;
    private EditText editText;
    public HashMap<String, Cat> archiveCats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App app=(App)getApplication();
        Client client=app.client;
        try {
            archiveCats = client.archive.getArchives();
        } catch (APIException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
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
                Cat cat=archiveCats.get(String.valueOf(position+1));
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

//            switch(editText.getText().toString()){
//                case "山岚": bundle.putInt("catId", 0);break;
//                case "李美人": bundle.putInt("catId",1);break;
//                case "小芝麻": bundle.putInt("catId",2);break;
//                default : Toast.makeText(ArchiveActivity.this, "查无此猫", Toast.LENGTH_SHORT).show();return;
//            }

        }
    }
}