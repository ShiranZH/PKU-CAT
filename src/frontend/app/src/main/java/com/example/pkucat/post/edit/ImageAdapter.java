package com.example.pkucat.post.edit;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.pkucat.R;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private static final int CHOOSE_PHOTO = 1;
    private static final int TAKE_PHOTO = 2;
    private static final int RECORD_VIDEO = 3;
    private final int MAX_IMAGE_NUM = 9;

    private Context context;
    private ArrayList<Uri> uriArrayList;

    public ImageAdapter(Context context, ArrayList<Uri> uriArrayList) {
        this.context = context;
        this.uriArrayList = uriArrayList;
    }

    @Override
    public int getCount() {
        int size = uriArrayList.size();
        if (size >= MAX_IMAGE_NUM) {
            return size;
        } else {
            return size+1;
        }
    }

    @Override
    public Object getItem(int position) {
        return uriArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.content_post_edit, null);
            imageView = convertView.findViewById(R.id.post_edit_pic);
            convertView.setTag(imageView);
        } else {
            imageView = (ImageView) convertView.getTag();
        }

        if (position < uriArrayList.size()) {
            imageView.setImageURI(uriArrayList.get(position));
        } else {
            imageView.setBackgroundResource(R.drawable.ic_post_edit_add);
        }

        return convertView;
    }


}
