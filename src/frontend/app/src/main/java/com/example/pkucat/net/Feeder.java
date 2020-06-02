package com.example.pkucat.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Feeder {
    private String[] feeders;
    
    public void refresh() throws APIException {
        try {
            byte[] ret = Session.get("/user/feeder/feeders", null);
            JSONObject retData = new JSONObject(new String(ret));

            if (retData.getInt("code") != 200)
                throw new APIException(retData);
            JSONArray feederArray = retData.getJSONObject("data").getJSONArray("feeders");
            feeders = new String[feederArray.length()];
            for (int i = 0; i < feederArray.length(); ++i) {
                feeders[i] = String.valueOf(feederArray.getInt(i));
            }
        }  catch (JSONException e) {
            e.printStackTrace();
            throw new APIException("404", "网络错误");
        } catch (APIException e) {
            throw e;
        }
    }
    
    public String[] getFeeders() throws APIException {
        if (feeders == null || feeders.length == 0)
            refresh();
        return feeders;
    }
    
    public Apply[] getApplys() throws APIException {
        try {
            byte[] ret = Session.get("/user/feeder/applys", null);
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
            JSONArray applyArray = retData.getJSONObject("data").getJSONArray("applies");
            Apply[] applys = new Apply[applyArray.length()];
            for (int i = 0; i < applyArray.length(); ++i) {
                applys[i] = new Apply();
                applys[i].applyID = String.valueOf(applyArray.getJSONObject(i).getInt("applyID"));
                applys[i].userID = String.valueOf(applyArray.getJSONObject(i).getInt("feederID"));
                applys[i].catID = String.valueOf(applyArray.getJSONObject(i).getInt("catID"));
            }
            return applys;
        }  catch (JSONException e) {
            e.printStackTrace();
            throw new APIException("404", "网络错误");
        } catch (APIException e) {
            throw e;
        }
    }
    
    public void agreeApply(String applyID) throws APIException {
        try {
            JSONObject data = new JSONObject();
            data.put("applyID", applyID);
            byte[] ret = Session.post("/user/feeder/agree", data, null);
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
        }  catch (JSONException e) {
            e.printStackTrace();
            throw new APIException("404", "网络错误");
        } catch (APIException e) {
            throw e;
        }
    }
    
    public void refuseApply(String applyID) throws APIException {
        try {
            JSONObject data = new JSONObject();
            data.put("applyID", applyID);
            byte[] ret = Session.delete("/user/feeder/apply", data);
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
        }  catch (JSONException e) {
            e.printStackTrace();
            throw new APIException("404", "网络错误");
        } catch (APIException e) {
            throw e;
        }
    }
    
    public void addApply(String catID) throws APIException {
        try {
            JSONObject data = new JSONObject();
            data.put("catID", catID);
            byte[] ret = Session.post("/user/feeder/apply", data, null);
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
        }  catch (JSONException e) {
            e.printStackTrace();
            throw new APIException("404", "网络错误");
        } catch (APIException e) {
            throw e;
        }
    }
}