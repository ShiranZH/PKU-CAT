package com.example.pkucat.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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

public class Client {
    private String baseUrl;
    
    public User user;
    public Archive archive;
    public PostManager post;
    public Feeder feeder;
    
    public Client(String protocol, String serverIP, String host)
    {
        this.baseUrl = protocol + "://" + serverIP + ":" + host;
        
        Session.setBaseUrl(baseUrl);
        this.user = new User();
        this.archive = new Archive();
        this.post = new PostManager();
        this.feeder = new Feeder();
    }

    public String[] uploadPicture(File[] files) throws APIException {
        return Session.uploadPicture(files);
    }
    
    public String[] uploadVideo(File[] files) throws APIException {
        return Session.uploadVideo(files);
    }
}
