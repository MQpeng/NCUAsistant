package com.study.pengshao.myclassinfoinncu.http_thread;

import android.content.Context;

import android.content.SharedPreferences;

import com.study.pengshao.myclassinfoinncu.Config;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by PengShao on 2016/7/30.
 */
public class HttpPostThread extends Thread{
    protected String userAccount;
    protected String userPassword;
    protected String cookie = "";
    protected Context context;
    private String urlPath = "http://218.64.56.18/jsxsd/xk/LoginToXk";
    protected boolean postSuccess;

    public HttpPostThread(Context context , String userAccount,
                          String userPassword){
        this.context = context;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
    }
    protected void doPost(){
        String encode = Config.parseEncode(userAccount,userPassword);
        try {
            String data = "userAccount="+ URLEncoder.encode(userAccount, "UTF-8")
                    + "&userPassword="+URLEncoder.encode(userPassword, "UTF-8")
                    + "&encoded="+URLEncoder.encode(encode, "UTF-8");
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);

            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Cookie",cookie);

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();
            cookie = urlConnection.getHeaderField("Set-Cookie");

            urlConnection.connect();
            int codePost = urlConnection.getResponseCode();
            if(codePost == 302) {
                postSuccess = true;
                if(cookie.isEmpty()){
                    postSuccess = false;
                }else {
                    cookie = cookie.substring(0, cookie.indexOf(';'));
                }
            }else{
                postSuccess = false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        doPost();
    }
}
