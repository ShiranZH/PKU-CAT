package com.example.pkucat.net;

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

public class Client {
    private String serverIP;
    private String protocol;
    private String baseUrl;
    private String host;
    private Session session;
    
    public User user;
    
    Client(String protocol, String serverIP, String host)
    {
        this.protocol = protocol;
        this.serverIP = serverIP;
        this.host = host;
        this.baseUrl = protocol + "://" + serverIP + ":" + host;
        
        this.session = new Session();
        this.user = new User(session, baseUrl);
    }

}
