package com.study.pengshao.myclassinfoinncu.http_thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by PengShao on 2016/8/3.
 */
public class HttpDoGet {
    private String cookies;
    private boolean isGetSuccess;

    public HttpDoGet(String cookies){
        this.cookies = cookies;
    }

    public HttpURLConnection doGetRun(String urlPath){
        URL url = null;
        try {
            url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Cookie",cookies);
            conn.setDoInput(true);
            conn.connect();

            if(conn.getResponseCode() == 200){
                isGetSuccess = true;
                return conn;
            }else {
                isGetSuccess = false;
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String downloadURL(String url){
        HttpURLConnection connection = doGetRun(url);
        if(isGetSuccess){
            InputStream is = null;
            try {
                is = connection.getInputStream();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(is,"utf-8"));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                reader.close();

                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
