package com.example.pkucat.net;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Archive {
    private HashMap<String, Cat> cats;
    
    Archive() {
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
        try {
            byte[] ret = Session.get("/user/archives", null);
            JSONObject retData = new JSONObject(new String(ret));

            if (retData.getInt("code") != 200)
                throw new APIException(retData);
            this.cats.clear();
            JSONArray catArray = retData.getJSONObject("data").getJSONArray("catList");
            for (int i = 0; i < catArray.length(); ++i) {
                String catID = String.valueOf(catArray.getJSONObject(i).getInt("catID"));
                this.cats.put(catID, new Cat(catArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new APIException("404", "·µ»ØÖµ´íÎó");
        } catch (APIException e) {
            throw e;
        }
    }
}
