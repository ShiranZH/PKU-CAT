package com.example.pkucat.net;
import org.json.JSONException;
import org.json.JSONObject;

public class APIException extends Exception {
    private static final long serialVersionUID = 6040399066607525708L;
    private String code;
    private String description;
    
    APIException(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    APIException(JSONObject ret) {
        try {
            this.code = String.valueOf(ret.getInt("code"));
            this.description = ret.getJSONObject("data").getString("msg");
        }
        catch (JSONException e)
        {
            this.code = "404";
            this.description = "返回值错误";
        }
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
}
