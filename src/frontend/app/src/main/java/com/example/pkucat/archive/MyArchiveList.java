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
import com.example.pkucat.R;
import com.example.pkucat.net.Cat;

import java.util.HashMap;

public class MyArchiveList extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Cat cats[];
    private int size;
    MyArchiveList(Context context, HashMap<String, Cat> archiveCats, String poistion2id[]){
        this.mContext = context;
        this.size = archiveCats.size();
        cats = new Cat[this.size];
        for (int i=0;i<this.size;i++) {
            this.cats[i]=archiveCats.get(poistion2id[i]);
        }
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder{
        public ImageView imageView;
        public TextView itemTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.archive_item,null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.catview);
            holder.itemTitle = (TextView) convertView.findViewById(R.id.item_title);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //给控件赋值
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform();
        holder.itemTitle.setText(cats[position].name);
        Glide.with(mContext).load(cats[position].getAvatar()).apply(mRequestOptions).into(holder.imageView);
        return convertView;
    }
}