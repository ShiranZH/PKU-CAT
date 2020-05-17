package com.example.pkucat.post.list;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.pkucat.R;
import com.example.pkucat.post.PostEntity;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private View view;
    private ArrayList<PostEntity> postEntities;
    ArrayList<View> viewList = new ArrayList<>();

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
        final PostEntity data = postEntities.get(position);
        holder.mUserName.setText(data.getUserName());
        holder.mPostTime.setText(data.getPostTime());
        holder.mPostContent.setText(data.getPostContent());
        holder.mAvaterPath.setImageURI(data.getAvatarPath());

        for (int i = 0; i < data.getImageNum(); i++) {
            view = LayoutInflater.from(context).inflate(R.layout.content_post_image, null);
            viewList.add(view);
        }

        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View view, Object object) {
                // TODO Auto-generated method stub
                return view == object;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return data.getImageNum();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                view = LayoutInflater.from(context).inflate(R.layout.content_post_image, container, false);
                ImageView imageView = view.findViewById(R.id.post_image);
                imageView.setImageURI(Uri.parse(data.getImagePath().get(position)));
                container.addView(view);
                return view;
            }
        };
        holder.mPostImage.setAdapter(pagerAdapter);

        holder.mIndicator.setViewPager(holder.mPostImage);

        // optional
        pagerAdapter.registerDataSetObserver(holder.mIndicator.getDataSetObserver());
    }

    @Override
    public int getItemCount() {
        return postEntities.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mUserName, mPostTime, mPostContent;
        private ImageView mAvaterPath;
        private ViewPager mPostImage;
        private CircleIndicator mIndicator;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mAvaterPath = itemView.findViewById(R.id.post_user_avatar);
            mUserName = itemView.findViewById(R.id.post_user_name);
            mPostTime = itemView.findViewById(R.id.post_time);
            mPostContent = itemView.findViewById(R.id.post_content);
            mPostImage = itemView.findViewById(R.id.post_collection);
            mIndicator = itemView.findViewById(R.id.post_indicator);

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
