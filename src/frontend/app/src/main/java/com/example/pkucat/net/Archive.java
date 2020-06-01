package com.example.pkucat.net;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class Archive {
    private Session session;
    private String baseUrl;
    private HashMap<String, Cat> cats;
    
    Archive(Session sess, String baseUrl) {
        this.session = sess;
        this.baseUrl = baseUrl;
        cats = new HashMap<String, Cat>();
    }
    
    public Cat getArchive(String catID) {
        if (!cats.containsKey(catID))
            return null;
        return cats.get(catID);
    }
    
    public HashMap<String, Cat> getArchives() throws APIException {
        if (cats.size() == 0)
            refreshCats();
        return cats;
    }
    
    public void refreshCats() throws APIException {
        System.out.println("refreshCats begin---------------");
        try {
            byte[] ret = session.get(baseUrl + "/archives", null);

            System.out.println(new String(ret));
            JSONObject retData = new JSONObject(new String(ret));

            if (retData.getInt("code") != 200)
                throw new APIException(retData);
//            JSONObject profileJSON = retData.getJSONObject("data").getJSONObject("profile");
//            UserProfile userProfile = new UserProfile(profileJSON, session);
        } catch (JSONException e) {
            throw new APIException("404", "·µ»ØÖµ´íÎó");
        } catch (APIException e) {
            throw e;
        }
        System.out.println("refreshCats end-----------------");
    }
}
    