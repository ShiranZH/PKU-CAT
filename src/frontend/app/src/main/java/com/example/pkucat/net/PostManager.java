package com.example.pkucat.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostManager {

    public Post addPost(Post post) {
        return null;
    }

    public void delPost(String postID) {

    }

    public Post[] getPosts(int num, int start, int commentNum) throws APIException {
        try {
            JSONObject data = new JSONObject();
//            data.put("num", String.valueOf(num));
//            data.put("start", String.valueOf(start));
//            data.put("commentNum", String.valueOf(commentNum));
            byte[] ret = Session.get("/user/post/posts", data);

            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
            int postCnt = retData.getJSONObject("data").getInt("downloadCount");
            Post[] posts = new Post[postCnt];
            JSONArray postArray = retData.getJSONObject("data").getJSONArray("posts");
            for (int i = 0; i < postArray.length(); ++i) {
                posts[i] = new Post(postArray.getJSONObject(i));
            }

            return posts;
        } catch (JSONException e) {
            throw new APIException("404", "返回值错误");
        } catch (APIException e) {
            throw e;
        }
    }

    public Post[] getPosts() throws APIException {
        return getPosts(10, 1, 10);
    }

    public Post[] searchPosts(String userID, String catID, String keyword, int commentNum) {
        return null;
    }
}