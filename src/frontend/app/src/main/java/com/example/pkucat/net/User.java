package com.example.pkucat.net;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private boolean isLogin = false;
    private UserProfile profile = null;
    private static HashMap<String, UserProfile> users = new HashMap<String, UserProfile>();
    
    
    public void register(String email) throws APIException {
        try {
            JSONObject data = new JSONObject();
            data.put("email", email);
            byte[] ret = Session.post(Session.baseUrl + "/user/register", data, null);
            if (ret == null)
                throw new APIException("404", "网络错误");
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
        } catch (JSONException e) {
            throw new APIException("404", "返回值错误");
        } catch (APIException e) {
            throw e;
        }
    }
    
    public void getPasswordCode(String email) throws APIException {
        try {
            JSONObject data = new JSONObject();
            data.put("email", email);
            byte[] ret = Session.get(Session.baseUrl + "/user/password", data);
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
        } catch (JSONException e) {
            throw new APIException("404", "返回值错误");
        } catch (APIException e) {
            throw e;
        }
    }
    
    public void setPassword(String email, String password, String code) throws APIException {
        try {
            JSONObject data = new JSONObject();
            data.put("email", email);
            data.put("password", password);
            data.put("verificationCode", code);
            byte[] ret = Session.post(Session.baseUrl + "/user/password", data, null);
            if (ret == null)
                throw new APIException("404", "网络错误");
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
        } catch (JSONException e) {
            throw new APIException("404", "返回值错误");
        } catch (APIException e) {
            throw e;
        }
    }
    
    public UserProfile registerValidation(String email,
            String password, String username, String code) throws APIException {
        try {
            JSONObject data = new JSONObject();
            data.put("email", email);
            data.put("password", password);
            data.put("username", username);
            data.put("verificationCode", code);
            byte[] ret = Session.post("/user/register_validation", data, null);
            if (ret == null)
                throw new APIException("404", "网络错误");
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
            JSONObject profileJSON = retData.getJSONObject("data").getJSONObject("profile");
            profile = new UserProfile(profileJSON);
            users.put(profile.userID, profile);
            isLogin = true;
            return profile;
        } catch (JSONException e) {
            throw new APIException("404", "返回值错误");
        } catch (APIException e) {
            throw e;
        }
    }
    
    public boolean isLogin() {
        return isLogin;
    }
    
    public UserProfile getProfile() throws APIException {
        return profile;
    }
    
    public static UserProfile getProfile(String userID) throws APIException  {
        if (users.containsKey(userID))
            return users.get(userID);
        try {
            JSONObject data = new JSONObject();
            data.put("userID", userID);
            byte[] ret = Session.get("/user/profile", data);
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
            JSONObject profileJSON = retData.getJSONObject("data").getJSONObject("profile");
            UserProfile userProfile = new UserProfile(profileJSON);
            users.put(userProfile.userID, userProfile);
            return userProfile;
        } catch (JSONException e) {
            throw new APIException("404", "返回值错误");
        } catch (APIException e) {
            throw e;
        }
    }

    public UserProfile login(String email, String password) throws APIException {
        try {
            JSONObject data = new JSONObject();
            data.put("email", email);
            data.put("password", SHA256.getSHA256(password));
            byte[] ret = Session.post("/user/login", data, null);
            if (ret == null)
                throw new APIException("404", "网络错误");
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
            JSONObject profileJSON = retData.getJSONObject("data").getJSONObject("profile");

            profile = new UserProfile(profileJSON);
            isLogin = true;
            users.put(profile.userID, profile);

            return profile;
        }
        catch (JSONException e)
        {
            throw new APIException("404", "返回值错误");
        }
        catch (APIException e)
        {
            throw e;
        }
    }
    
    public void logout() throws APIException {
        try {
            isLogin = false;
            byte[] ret = Session.post("/user/logout", null, null);
            if (ret == null)
                throw new APIException("404", "网络错误");
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200) {
                throw new APIException(retData);
            }
        }
        catch (JSONException e)
        {
            throw new APIException("404", "返回值错误");
        }
        catch (APIException e)
        {
            throw e;
        }
    }
}

class SHA256 {
    public static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodestr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodestr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}

