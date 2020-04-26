package com.example.pkucat.ui.post.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pkucat.R;
import com.example.pkucat.ui.post.PostEntity;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PostViewModel postViewModel;

    private View view;
    public RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private ArrayList<PostEntity> postEntities;

    public static PostFragment newInstance(int index) {
        PostFragment fragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
            // 这里是对不同tab下的界面处理
            System.out.println(index);
        }
        postViewModel.setIndex(index);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post, container, false);
        /*
        final TextView textView = root.findViewById(R.id.section_label);
        postViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
        */
        initData();
        initRecyclerView();
        return view;
    }

    private void initData() {
        postEntities = new ArrayList<>();
        // 获取动态
        for (int i = 0; i < 15; i++) {
            PostEntity postEntity = new PostEntity();
            postEntity.setAvatarPath("Avatar");
            postEntity.setUserName("User"+i);
            postEntity.setPostTime("Time");
            postEntity.setPostContent("Post"+i);
            postEntity.setImagePath("Image Path!");
            postEntities.add(postEntity);
        }
    }

    private void initRecyclerView() {
        mRecyclerView = view.findViewById(R.id.recyclerview_post);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity(), postEntities);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        // layout: vertical, horizontal, ...
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        // dividing line
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, PostEntity data) {
                Toast.makeText(getActivity(), "Here it is: "+data.getAvatarPath(), Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }
}
