package com.example.pkucat.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
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
import java.util.UUID;

public class Session {
    private static String cookie = "";
    public static String baseUrl;
    
    public static void setBaseUrl(String url) {
        baseUrl = url;
    }

    public static String json2Url(JSONObject json) throws JSONException, UnsupportedEncodingException {
        if (null == json) {
            return "";
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
            url.append(URLEncoder.encode(key, "utf-8")).append("=");
            String value = json.getString(key);
            url.append(URLEncoder.encode(value, "utf-8"));
        }
        return url.toString();
    }
    
    public static String map2Url(Map<String, String> paramToMap) {
        if (null == paramToMap || paramToMap.isEmpty()) {
            return null;
        }
        StringBuffer url  = new StringBuffer();
        boolean      isfist = true;
        for (Map.Entry<String, String> entry : paramToMap.entrySet()) {
            if (isfist) {
                isfist = false;
            } else {
                url.append("&");
            }
            url.append(entry.getKey()).append("=");
            String value = entry.getValue();
            url.append(value);
        }
        return url.toString();
    }

    public static byte[] post(String urlStr, JSONObject data, HashMap<String, List<File>> files){
        byte[] response = null;
        PostThread thread = new PostThread(baseUrl+urlStr, data, files, cookie);
        thread.start();
        
        try {
            thread.join();
            response = thread.response;
            if (thread.cookie != null) 
                cookie = thread.cookie;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return response;
    }
    
    public static byte[] put(String urlStr, JSONObject data){
        return request(urlStr, data, "PUT");
    }
    
    public static byte[] delete(String urlStr, JSONObject data){
        return request(urlStr, data, "DELETE");
    }
    
    public static byte[] get(String urlStr, JSONObject data){
        return request(urlStr, data, "GET");
    }
  
    public static byte[] request(String urlStr, JSONObject data, String method){
        byte[] response = null;
        RequestThread thread = new RequestThread(baseUrl+urlStr, data, cookie, method);
        thread.start();
        
        try {
            thread.join();
            response = thread.response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    
    public static String[] uploadPicture(File[] files) throws APIException {
        HashMap<String, List<File>> fs = new HashMap<String, List<File>>();
        fs.put("picture", new ArrayList<File>());
        for (int i = 0; i < files.length; ++i) {
            fs.get("picture").add(files[i]);
        }
        try {
            byte[] ret = Session.post("/file", null, fs);
            
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
            
            JSONArray urlArray = retData.getJSONObject("data").getJSONArray("picture");
            
            String[] urls = new String[urlArray.length()];
            for (int i = 0; i < urlArray.length(); ++i) {
                urls[i] = urlArray.getString(i);
            }
            
            return urls;
        } catch (JSONException e) {
            throw new APIException("404", "返回值错误");
        } catch (APIException e) {
            throw e;
        }
    }
    
    public static String[] uploadVideo(File[] files) throws APIException {
        HashMap<String, List<File>> fs = new HashMap<String, List<File>>();
        fs.put("picture", new ArrayList<File>());
        for (int i = 0; i < files.length; ++i) {
            fs.get("video").add(files[i]);
        }
        try {
            byte[] ret = Session.post("/file", null, fs);
            
            JSONObject retData = new JSONObject(new String(ret));
            if (retData.getInt("code") != 200)
                throw new APIException(retData);
            
            JSONArray urlArray = retData.getJSONObject("data").getJSONArray("video");
            
            String[] urls = new String[urlArray.length()];
            for (int i = 0; i < urlArray.length(); ++i) {
                urls[i] = urlArray.getString(i);
            }
            
            return urls;
        } catch (JSONException e) {
            throw new APIException("404", "返回值错误");
        } catch (APIException e) {
            throw e;
        }
    }
}


class RequestThread extends Thread {
    public byte[] response;
    public String cookie;
    
    private String urlStr;
    private JSONObject data;
    private String method;
    
    public RequestThread(String urlStr, JSONObject data, String cookie, String method) {
        this.response = null;
        this.urlStr = urlStr;
        this.data = data;
        this.cookie = cookie;
        this.method = method;
    }
    public void run() {
        try {
            URL url = new URL(urlStr+"?"+Session.json2Url(data));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("cookie", cookie);
            connection.setSSLSocketFactory(SSLSocketClient.getSSLSocketFactory());
            connection.setHostnameVerifier(SSLSocketClient.getHostnameVerifier());
            connection.connect();
            if (connection.getResponseCode() != 200)
                response = null;
            
            response = IOUtils.toByteArray(connection.getInputStream());
            
            connection.disconnect();
        } catch (Exception e){
            e.printStackTrace();
            response = null;
        }
    }
}

class PostThread extends Thread {
    public byte[] response;
    public String cookie;
    
    private String urlStr;
    private JSONObject data;
    private HashMap<String, List<File>> files;
    
    public PostThread(String urlStr,
            JSONObject data, HashMap<String, List<File>> files, String cookie) {
        this.response = null;
        this.urlStr = urlStr;
        this.data = data;
        this.files = files;
        this.cookie = cookie;
    }
    public void run() {
        try {
            URL url = new URL(urlStr);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            String boundary = UUID.randomUUID().toString().replace("-", "");
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("cookie", cookie);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
            connection.setSSLSocketFactory(SSLSocketClient.getSSLSocketFactory());
            connection.setHostnameVerifier(SSLSocketClient.getHostnameVerifier());
            
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            
            if (data != null) {
                Iterator<String> it = data.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    String value = data.getString(key);
                    
                    out.write(("--"+boundary+"\r\n").getBytes());
                    out.write(("Content-Disposition: form-data; name=\""+URLEncoder.encode(key, "utf-8")+"\"").getBytes());
                    out.write("\r\n\r\n".getBytes());
                    out.write(value.getBytes());
                }
            }
            if (files != null) {
                for (Map.Entry<String, List<File>> entry : files.entrySet()) {
                    String key = entry.getKey();
                    List<File> fileList = entry.getValue();
                    
                    for (File file: fileList) {
                        out.write(("--"+boundary+"\r\n").getBytes());
                        out.write(("Content-Disposition: form-data; "
                                + "name=\""+URLEncoder.encode(key, "utf-8")+"\"; "
                                + "filename=\""+file.getName()+"\";").getBytes());
                        out.write("\r\n\r\n".getBytes());
                        FileInputStream in = new FileInputStream(file);
                        out.write(IOUtils.toByteArray(in));
                        in.close();
                    } 
                }
            }
            out.write(("\r\n--"+boundary+"--\r\n").getBytes());
            
            out.flush();
            connection.connect();
            
            if (connection.getResponseCode() != 200)
                response = null;
            else {
                String response_cookie = connection.getHeaderField("Set-Cookie");
                if (response_cookie != null)
                    cookie = response_cookie;
                response = IOUtils.toByteArray(connection.getInputStream());
                
                connection.disconnect();
            }
        }catch (Exception e){
            e.printStackTrace();
            response = null;
        }
    }
}

class SSLSocketClient {
    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {}

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {}

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }
        };
        return trustAllCerts;
    }

    public static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        return hostnameVerifier;
    }
}
