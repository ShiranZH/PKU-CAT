package com.example.pkucat.ui.setting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


final class Client {
    static private JSONObject ret;
    static private JSONObject request;
    static private URL url;

    public static String json2Url(JSONObject json) throws JSONException {
        if (null == json) {
            return null;
        }
        StringBuffer url  = new StringBuffer();
        boolean      isfist = true;
        for (Iterator<String> it = json.keys(); it.hasNext(); ) {
            String key = it.next();
            if (isfist) {
                isfist = false;
            } else {
                url.append("&");
            }
            url.append(key).append("=");
            String value = json.getString(key);
            // if (!StringUtils.isEmpty(value)) {
            url.append(value);
            // }
        }
        return url.toString();
    }

    public static JSONObject post(JSONObject json, URL dst, final String cookie){
        request = json;
        url = dst;
        Thread t1 = new Thread(){
            public void run(){
                try {
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    if(null != cookie) connection.setRequestProperty("cookie", cookie);
                    connection.setSSLSocketFactory(SSLSocketClient.getSSLSocketFactory());
                    connection.setHostnameVerifier(SSLSocketClient.getHostnameVerifier());
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                    bw.write(json2Url(request));
                    bw.flush();
                    String response_cookie = connection.getHeaderField("Set-Cookie");
                    StringBuilder strb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while((line = br.readLine()) != null){
                        strb.append(line);
                    }
                    connection.disconnect();
                    ret = new JSONObject(strb.toString());
                    if(null != response_cookie){
                        ret.put("cookie", response_cookie);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        t1.start();
        try{
            t1.join();
        }catch (InterruptedException inter){
            inter.printStackTrace();
        }
        return ret;
    }

    public static JSONObject put(JSONObject json, URL dst, final String cookie){
        request = json;
        url = dst;
        Thread t1 = new Thread(){
            public void run(){
                try {
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("PUT");
                    if(null != cookie) connection.setRequestProperty("cookie", cookie);
                    connection.setSSLSocketFactory(SSLSocketClient.getSSLSocketFactory());
                    connection.setHostnameVerifier(SSLSocketClient.getHostnameVerifier());
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                    bw.write(json2Url(request));
                    bw.flush();
                    StringBuilder strb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while((line = br.readLine()) != null){
                        strb.append(line);
                    }
                    connection.disconnect();
                    ret = new JSONObject(strb.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        t1.start();
        try{
            t1.join();
        }catch (InterruptedException inter){
            inter.printStackTrace();
        }
        return ret;
    }

    public static Bitmap getBmp(URL src){
        Bitmap ret = null;
        try{
            HttpsURLConnection connection = (HttpsURLConnection) src.openConnection();
            connection.setRequestMethod("GET");
            connection.setSSLSocketFactory(SSLSocketClient.getSSLSocketFactory());
            connection.setHostnameVerifier(SSLSocketClient.getHostnameVerifier());
            InputStream is = connection.getInputStream();
            ret = BitmapFactory.decodeStream(is);
            is.close();
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }
}
class SSLSocketClient {

    //获取这个SSLSocketFactory
    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //获取TrustManager
    private static TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        // return null; 或者
                        return new X509Certificate[]{}; // 空实现
                    }
                }
        };
        return trustAllCerts;
    }

    //获取HostnameVerifier
    public static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                // true表示信任所有域名
                return true;
            }
        };
        return hostnameVerifier;
    }
}

