package com.example.pkucat.ui.setting;

import android.util.JsonReader;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;



final class Client {

    public static JSONObject post(JSONObject json, URL dst){
        try {
            HttpURLConnection connection = (HttpURLConnection) dst.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(json.toString());
            bw.flush();
            StringBuilder strb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = br.readLine()) != null){
                strb.append(line);
            }
            return new JSONObject(strb.toString());
        }catch (Exception e){
            return null;
        }
    }
}
