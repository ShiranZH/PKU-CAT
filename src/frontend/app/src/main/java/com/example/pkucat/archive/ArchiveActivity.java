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

import com.example.pkucat.R;

public class ArchiveActivity extends Activity {
    private Button bSearchArchive;
    private ListView archiveList;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        bSearchArchive = (Button) findViewById(R.id.searchArchive_button);
        editText=(EditText) findViewById(R.id.search_text);
        setListeners();
        archiveList = (ListView) findViewById(R.id.archive_list);
        archiveList.setAdapter(new MyArchiveList(ArchiveActivity.this));
        archiveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent;
                intent = new Intent(ArchiveActivity.this, SoleArchive.class);
                Bundle bundle = new Bundle();
                bundle.putInt("catId", position);
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
            switch(editText.getText().toString()){
                case "山岚": bundle.putInt("catId", 0);break;
                case "李美人": bundle.putInt("catId",1);break;
                case "小芝麻": bundle.putInt("catId",2);break;
                default : Toast.makeText(ArchiveActivity.this, "查无此猫", Toast.LENGTH_SHORT).show();return;
            }
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
