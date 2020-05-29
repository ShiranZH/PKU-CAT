import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private Session session;
    private String baseUrl;
    private boolean isLogin = false;
    private UserProfile profile = null;
    
    
    User(Session sess, String url) {
        session = sess;
        baseUrl = url;
    }
    
    public boolean isLogin() {
        return isLogin;
    }
    
    public UserProfile getProfile() {
        return profile;
    }
    
    public UserProfile getProfile(String userID) {
        return null;
    }

    public UserProfile login(String email, String password) throws APIException {
        JSONObject data = new JSONObject();
        data.put("email", email);
        data.put("password", SHA256.getSHA256(password));
        byte[] ret = session.post(baseUrl + "/user/login", data, null);
        if (ret == null)
            throw new APIException("404", "网络错误");
        JSONObject retData = new JSONObject(new String(ret));
        if (retData.getInt("code") != 200)
            throw new APIException(retData);
        JSONObject profileJSON = retData.getJSONObject("data").getJSONObject("profile");
        
        profile = new UserProfile(profileJSON, session);
        isLogin = true;
        
        return profile;
    }
    
    public void logout() {
        
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
                // 1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}

