package com.example.pkucat.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
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
            data.put("limit", String.valueOf(num));
            data.put("start", String.valueOf(start));
            data.put("commentNum", String.valueOf(commentNum));
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
            throw new APIException("404", "·µ»ØÖµ´íÎó");
        } catch (APIException e) {
            throw e;
        }
    }

    public void addPost(String text) throws APIException, JSONException {
        JSONObject data = new JSONObject();
        data.put("text", text);
        data.put("isVideo", "0");
        byte[] ret = Session.post("/user/post/post", data, null);

        JSONObject retData = new JSONObject(new String(ret));
        if (retData.getInt("code") != 200)
            throw new APIException(retData);
    }

    public void addPost(String text, File file, boolean isVideo) throws APIException, JSONException {
        File[] files = new File[]{file};
        String[] urls = Session.uploadPicture(files);
        JSONObject data = new JSONObject();
        data.put("multimediaContent", urls[0]);
        if (isVideo)
            data.put("isVideo", "1");
        else
            data.put("isVideo", "0");

        if (text != null)
            data.put("text", text);

        try {
            byte[] ret = Session.post("/user/post/post", data, null);
            if (ret == null)
                throw new APIException("404", "ÍøÂç´íÎó");
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
        } catch (JSONException e) {
            throw new APIException("404", "·µ»ØÖµ´íÎó");
        } catch (APIException e) {
            throw e;
        }
    }


    public Post[] searchPosts(String userID, String catID, String keyword, int commentNum) {
        return null;
    }
}