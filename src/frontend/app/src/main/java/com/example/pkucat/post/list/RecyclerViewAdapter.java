package com.example.pkucat.post.list;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.pkucat.App;
import com.example.pkucat.R;
import com.example.pkucat.net.Client;
import com.example.pkucat.net.Comment;
import com.example.pkucat.net.Post;
import com.example.pkucat.post.PostEntity;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private View view;
    //private ArrayList<PostEntity> postEntities;
    private Post[] posts;
    ArrayList<View> viewList = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private Client client;

    // construct function
    public RecyclerViewAdapter(Context context, Post[] posts, Client client) {
        this.context = context;
        //this.postEntities = postEntities;
        this.posts = posts;
        this.client = client;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.content_post, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // 把获取的动态传递给页面
        /*
        final PostEntity data = postEntities.get(position);
        holder.mUserName.setText(data.getUserName());
        holder.mPostTime.setText(data.getPostTime());
        holder.mPostContent.setText(data.getPostContent());
        //holder.mAvatarPath.setImageURI(data.getAvatarPath());

         */
        final Post data = posts[position];
        holder.mUserName.setText(data.author.username);
        holder.mPostTime.setText(sdf.format(data.date));
        System.out.println("postcontent"+position+data.text);
        System.out.println(data.text.length() == 0);
        System.out.println(data.text=="");
        if (data.text == null || data.text == "" || data.text.length() == 0) {
            holder.mPostContent.setVisibility(View.GONE);
        } else {
            holder.mPostContent.setText(data.text);
        }
        holder.mAvatarPath.setImageBitmap(getPicFromBytes(data.author.getAvatar(), null));

        if (data.photoUrl == null || data.getPhoto() == null) {
            holder.mPostImage.setVisibility(View.GONE);
            holder.mIndicator.setVisibility(View.GONE);
        } else {
            // final int picNum = data.getImageNum();
            final int picNum = 1;
            System.out.println(position);

            for (int i = 0; i < picNum; i++) {
                view = LayoutInflater.from(context).inflate(R.layout.content_post_image, null);
                viewList.add(view);
            }


            PagerAdapter pagerAdapter = new PagerAdapter() {

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public int getCount() {
                    //return data.getImageNum();
                    return picNum;
                }

                @Override
                public void destroyItem(ViewGroup container, int position,
                                        Object object) {
                    //container.removeView(viewList.get(position % (data.getImageNum())));
                    container.removeView(viewList.get(position % picNum));
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    view = LayoutInflater.from(context).inflate(R.layout.content_post_image, container, false);
                    ImageView imageView = view.findViewById(R.id.post_image);
                    //imageView.setImageURI(Uri.parse(data.getImagePath().get(position)));
                    //imageView.setBackgroundResource(data.getImagePath().get(position));
                    imageView.setImageBitmap(getPicFromBytes(data.getPhoto(), null));
                    container.addView(view);
                    return view;
                }
            };
            holder.mPostImage.setAdapter(pagerAdapter);
            holder.mIndicator.setViewPager(holder.mPostImage);
            // optional
            //pagerAdapter.registerDataSetObserver(holder.mIndicator.getDataSetObserver());
        }


        ArrayList<Map<String, String>> commentList = new ArrayList<>();
        if (data.comments != null) {
            for (Comment comment:data.comments) {
                Map<String, String> keyValuePair = new HashMap<>();
                keyValuePair.put("ID", comment.author.username);
                keyValuePair.put("Time", sdf.format(comment.date));
                keyValuePair.put("Content", comment.text);
                commentList.add(keyValuePair);
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(this.context, commentList,
                    R.layout.content_post_comment, new String[] { "ID",
                    "Time", "Content" }, new int[] { R.id.post_comment_id,
                    R.id.post_comment_time, R.id.post_comment_text });
            holder.mComment.setAdapter(simpleAdapter);
        } else {
            holder.mComment.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        // return postEntities.size();
        return posts.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mUserName, mPostTime, mPostContent;
        private ImageView mAvatarPath;
        private ViewPager mPostImage;
        private CircleIndicator mIndicator;
        private ListView mComment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mAvatarPath = itemView.findViewById(R.id.post_user_avatar);
            mUserName = itemView.findViewById(R.id.post_user_name);
            mPostTime = itemView.findViewById(R.id.post_time);
            mPostContent = itemView.findViewById(R.id.post_content);
            mPostImage = itemView.findViewById(R.id.post_collection);
            mIndicator = itemView.findViewById(R.id.post_indicator);
            mComment = itemView.findViewById(R.id.post_comment_list);

            // TODO: 点赞功能
            ShineButton btLike = (ShineButton) itemView.findViewById(R.id.bt_like);
            btLike.init((Activity) context);
            btLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            ShineButton btStar = (ShineButton) itemView.findViewById(R.id.bt_star);
            btStar.init((Activity) context);


            // TODO: 评论功能
            ShineButton btComment = (ShineButton) itemView.findViewById(R.id.bt_comment);
            btComment.init((Activity) context);
            btComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        //onItemClickListener.OnItemClick(view, postEntities.get(getLayoutPosition()));
                        onItemClickListener.OnItemClick(view, posts[getLayoutPosition()]);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        /**
         * 接口中的点击每一项的实现方法，参数自己定义
         *  @param view 点击的item的视图
         * @param data 点击的item的数据
         */
        public void OnItemClick(View view, Post data);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 将字节数组转换为ImageView可调用的Bitmap对象
     * @param bytes
     * @param opts
     * @return Bitmap
     */
    public static Bitmap getPicFromBytes(byte[] bytes,
                                         BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }
}
