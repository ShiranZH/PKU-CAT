package com.example.pkucat.archive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pkucat.App;
import com.example.pkucat.R;
import com.example.pkucat.net.APIException;
import com.example.pkucat.net.Cat;
import com.example.pkucat.net.Client;

import java.util.HashMap;

public class AdministrationList extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int size;
    public String catnames[];
    AdministrationList(Context context,String names[], int size) {
        this.mContext = context;
        this.catnames = names;
        this.size = size;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return this.size;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    static class ViewHolder {
        public ImageView imageView;
        public TextView itemTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelationList.ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.archive_administration_item, null);
            holder = new RelationList.ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.catview);
            holder.itemTitle = (TextView) convertView.findViewById(R.id.item_title);
            convertView.setTag(holder);
        } else {
            holder = (RelationList.ViewHolder) convertView.getTag();
        }
        //给控件赋值
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform();
        holder.itemTitle.setText(catnames[position]);
        Glide.with(mContext).load(R.drawable.ic_post_text).apply(mRequestOptions).into(holder.imageView);
        return convertView;
    }
}
