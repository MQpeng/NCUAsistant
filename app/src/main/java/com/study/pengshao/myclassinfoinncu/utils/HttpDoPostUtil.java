package com.study.pengshao.myclassinfoinncu.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PengShao on 2016/9/3.
 */
public class HttpDoPostUtil {
    public static String downloadByPost(String path, String cookie, String data) {
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(false);
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);

            conn.setRequestProperty("Content-Type","application/json;utf-8");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2810.2 Safari/537.36");
            conn.setRequestProperty("Cookie",cookie);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();

            conn.connect();
            InputStream is = null;
            is = conn.getInputStream();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(is,"UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            reader.close();
            outputStream.close();

            return sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<String> doPostUrl(String path, String data){
        String cookie = "";
        List<String> cookies = new ArrayList<String>();
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(false);
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);

            conn.setRequestProperty("Content-Type","application/json;utf-8");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2810.2 Safari/537.36");
            conn.setRequestProperty("Cookie",cookie);

            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();

            String key = null;
            for(int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++){
                if(key.equalsIgnoreCase("Set-Cookie")){
                    cookie = conn.getHeaderField(i);
                    cookie = cookie.substring(0, cookie.indexOf("; "));
                    cookies.add(cookie);
                }
            }
            conn.connect();
            int codePost = conn.getResponseCode();
            if(codePost == 200) {
//                if(!cookies.isEmpty()){
//                    cookie = null;
//                    for(int i = 0;i<cookies.size();i++){
//                        cookie += cookies.get(i);
//                        if(i!=cookies.size()-1){
//                            cookie += ";";
//                        }
//                    }
//                    return cookie;
//                }
                return cookies;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean doPostUrlWithCookies(String path, String cookie,String data){

        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(false);
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);

            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("Cache-Control","no-cache");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2810.2 Safari/537.36");
            conn.setRequestProperty("Cookie",cookie);

            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();

            conn.connect();
            int codePost = conn.getResponseCode();
            if(codePost == 200) {
                return true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


}
