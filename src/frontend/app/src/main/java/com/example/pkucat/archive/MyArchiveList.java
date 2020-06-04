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
    MyArchiveList(Context context, HashMap<String, Cat> archiveCats){
        this.mContext = context;
        this.size = archiveCats.size();
        cats = new Cat[this.size];
        int i = 0;
        for (String key : archiveCats.keySet()) {
            this.cats[i]=archiveCats.get(key);
            ++i;
        }
        /*
        for (int i=1;i<=this.size;i++) {
            this.cats[i-1]=archiveCats.get(String.valueOf(i));
        }

         */
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return cats[position];
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
//        switch (position%3) {
//            case 0:
//                holder.itemTitle.setText("山岚");
//                Glide.with(mContext).load(R.drawable.catexp0).apply(mRequestOptions).into(holder.imageView);
//                break;
//            case 1:
//                holder.itemTitle.setText("李美人");
//                Glide.with(mContext).load(R.drawable.catexp1).apply(mRequestOptions).into(holder.imageView);
//                break;
//            case 2:
//                holder.itemTitle.setText("小芝麻");
//                Glide.with(mContext).load(R.drawable.catexp2).apply(mRequestOptions).into(holder.imageView);
//                break;
//
//        }
        holder.itemTitle.setText(cats[position].name);
        Glide.with(mContext).load(cats[position].getAvatar()).apply(mRequestOptions).into(holder.imageView);
        return convertView;
    }
}