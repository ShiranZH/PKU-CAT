package com.example.pkucat.post;

import android.net.Uri;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PostEntityTest {

    private PostEntity postEntity;

    public PostEntityTest() {
        this.postEntity = new PostEntity();
    }

    @Test
    public void getAvatarPath() {
        assertEquals(postEntity.getAvatarPath(), Uri.parse(""));
    }

    @Test
    public void getUserName() {
        assertEquals(postEntity.getUserName(), "");
    }

    @Test
    public void getPostTime() {
        assertEquals(postEntity.getPostTime(),"");
    }

    @Test
    public void getPostContent() {
        assertEquals(postEntity.getPostContent(), "");
    }

    @Test
    public void getImagePath() {
        assertEquals(postEntity.getImagePath(), new ArrayList<Integer>());
    }

}