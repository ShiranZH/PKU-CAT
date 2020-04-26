package com.example.pkucat.ui.post.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pkucat.R;
import com.example.pkucat.ui.post.PostEntity;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<PostEntity> postEntities;

    // construct function
    public RecyclerViewAdapter(Context context, ArrayList<PostEntity> postEntities) {
        this.context = context;
        this.postEntities = postEntities;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.content_post, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // 把获取的动态传递给页面
        PostEntity data = postEntities.get(position);
        holder.mUserName.setText(data.getUserName());
        holder.mPostTime.setText(data.getPostTime());
        holder.mPostContent.setText(data.getPostContent());
        holder.mAvaterPath.setImageURI(data.getAvatarPath());
    }

    @Override
    public int getItemCount() {
        return postEntities.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mUserName, mPostTime, mPostContent;
        private ImageView mAvaterPath;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mAvaterPath = itemView.findViewById(R.id.post_user_avatar);
            mUserName = itemView.findViewById(R.id.post_user_name);
            mPostTime = itemView.findViewById(R.id.post_time);
            mPostContent = itemView.findViewById(R.id.post_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.OnItemClick(view, postEntities.get(getLayoutPosition()));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        /*
         * 接口中的点击每一项的实现方法，参数自己定义
         *
         * @param view 点击的item的视图
         * @param data 点击的item的数据
         */
        public void OnItemClick(View view, PostEntity data);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
