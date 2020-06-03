package com.example.pkucat.manage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pkucat.R;
import com.example.pkucat.net.APIException;
import com.example.pkucat.net.Apply;
import com.example.pkucat.net.Client;

import java.util.ArrayList;

public class ApplyList extends BaseAdapter {

    private Context mContext;
    private Client client;

    private class ApplyInfo
    {
        String applyID;
        String userID;
        String catID;
        String userName;
        String catName;
        public ApplyInfo(){}
        public ApplyInfo(String applyID, String userID, String catID)
        {
            this.applyID = applyID;
            this.userID = userID;
            this.catID = catID;
            try {
                userName = client.user.getProfile(userID).username;
                catName = client.archive.getArchive(catID).name;
            }
            catch (APIException e)
            {
                Toast.makeText(mContext, e.getDescription(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if(obj == null) return false;
            if(obj instanceof ApplyInfo)
            {
                ApplyInfo o = (ApplyInfo) obj;
                return o.applyID.equals(applyID);
            }
            return false;
        }
    }
    ArrayList<ApplyInfo> applyInfoList;

    public ApplyList(Context mContext, Client client)
    {
        super();
        this.mContext = mContext;
        this.client = client;
        applyInfoList = new ArrayList<>();
        // example
        Apply[] applyList;
        try {
            applyList = client.feeder.getApplys();
            for(Apply apply: applyList)
            {
                applyInfoList.add(new ApplyInfo(apply.applyID, apply.userID, apply.catID));
            }
        }
        catch (APIException e)
        {
            Toast.makeText(mContext, e.getDescription(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getCount() {
        return applyInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return applyInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private void dealApply(final int index, final boolean result)
    {
        final ApplyInfo applyInfo = applyInfoList.get(index);
        final String name = applyInfo.userName;
        final String applyID = applyInfo.applyID;
        final String operation = result?"同意":"拒绝";
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage(operation+"用户"+name+"的申请请求吗？");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if(result)client.feeder.agreeApply(applyID);
                    else client.feeder.refuseApply(applyID);
                    applyInfoList.remove(index);
                    notifyDataSetChanged();
                    Toast.makeText(mContext,"操作成功！",Toast.LENGTH_SHORT).show();
                }
                catch (APIException e)
                {
                    Toast.makeText(mContext, "操作失败！ "+e.getDescription(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.apply_list_item,null);
        final String userName = applyInfoList.get(position).userName;
        final String catName = applyInfoList.get(position).catName;
        TextView textViewName = view.findViewById(R.id.admin_name);
        textViewName.setText(userName);
        TextView textViewLevel = view.findViewById(R.id.admin_cat_name);
        textViewLevel.setText(catName);

        Button agreeButton = view.findViewById(R.id.admin_agree_button);
        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealApply(position, true);
            }
        });
        Button refuseButton = view.findViewById(R.id.admin_refuse_button);
        refuseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealApply(position, false);
            }
        });
        return view;
    }
}
