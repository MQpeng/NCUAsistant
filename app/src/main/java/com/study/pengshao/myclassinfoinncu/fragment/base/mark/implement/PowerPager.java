package com.study.pengshao.myclassinfoinncu.fragment.base.mark.implement;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.study.pengshao.myclassinfoinncu.HostTestActivity;
import com.study.pengshao.myclassinfoinncu.R;
import com.study.pengshao.myclassinfoinncu.data_class.PowerData;
import com.study.pengshao.myclassinfoinncu.fragment.base.BasePager;
import com.study.pengshao.myclassinfoinncu.http_thread.HttpDoGet;
import com.study.pengshao.myclassinfoinncu.http_thread.ParseHTML;
import com.study.pengshao.myclassinfoinncu.utils.NetWorkUtil;
import com.study.pengshao.myclassinfoinncu.utils.ShowKeyBoard;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PengShao on 2016/8/15.
 */
public class PowerPager extends BasePager{
    private EditText mEditText;
    private Button submit;
    private TextView mTextView;
    private String powerUrl="http://222.204.3.210/ssdf/Account/LogOn";
    private String queryUrl = "http://222.204.3.210/ssdf/EEMQuery/EEMBalance";
    private String UserName;
    private String cookie="";
    private boolean postSuccess;
    private PowerData powerData;
    private Message message;
    private Handler handler;
    public PowerPager(HostTestActivity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity,R.layout.power_query,null);
        mEditText = (EditText) view.findViewById(R.id.power_et);
        submit = (Button) view.findViewById(R.id.power_bt);
        mTextView = (TextView) view.findViewById(R.id.help_power_tv);
        return view;
    }

    @Override
    public void initData() {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1 == -1){
                    Toast.makeText(mActivity,"请输入正确的房间号···",Toast.LENGTH_SHORT).show();
                    return;
                }
                powerData = (PowerData) msg.obj;
                mTextView.setText("补贴现金余额："+powerData.cashStudent+"元\n"
                        +"补贴电量："+powerData.powerStudent+"度\n"
                        +"现金余额："+powerData.cashReminder+"元\n"
                        +"剩余电量："+powerData.powerReminder+"度");
                Toast.makeText(mActivity,"更新成功!",Toast.LENGTH_SHORT).show();
            }
        };
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowKeyBoard.closeKeyBroad(mActivity,mEditText);
                if(NetWorkUtil.isNetworkConnected(mActivity)) {
                    UserName = mEditText.getText().toString();
                    if(UserName.isEmpty()){
                        Toast.makeText(mActivity,"房间号不能为空···",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    powerPost(powerUrl);
                }else {
                    Toast.makeText(mActivity,"网络似乎断开了···",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void powerPost(final String urlPath){

        new Thread(){
            @Override
            public void run() {
                String headData = null;
                String tempCookie = doPost(queryUrl);
                try {
                    headData = "UserName="+ URLEncoder.encode(UserName, "UTF-8");
                    URL url = new URL(urlPath);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setInstanceFollowRedirects(false);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setConnectTimeout(5000);

                    urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    urlConnection.setRequestProperty("Cookie",tempCookie);

                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    OutputStream outputStream = urlConnection.getOutputStream();
                    outputStream.write(headData.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    cookie = urlConnection.getHeaderField("Set-Cookie");
                    if(cookie==null){
                        postSuccess = false;
                    }else {
                        cookie = tempCookie+"; " + cookie.substring(0, cookie.indexOf(';'));
                    }
                    urlConnection.connect();
                    int codePost = urlConnection.getResponseCode();
                    if(codePost == 302) {
                        postSuccess = true;
                    }else{
                        postSuccess = false;
                    }
                    message = new Message();
                    if(postSuccess) {
                        HttpDoGet httpDoGet = new HttpDoGet(cookie);
                        String result = httpDoGet.downloadURL(queryUrl);
                        ParseHTML parseHTML = new ParseHTML();
                        List<String> keys = new ArrayList<String>();
                        keys.add("table");
                        keys.add("tr");
                        keys.add("td");
                        powerData = parseHTML.parsePowerInfoFromUrlData(result, keys, 0, false);
                        message.obj = powerData;
                        handler.sendMessage(message);
                    }else {
                        message.arg1 = -1;
                        handler.sendMessage(message);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private String doPost(String path){
        String cookie = null;
        try {
            URL url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);

            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Cookie", cookie);

            cookie = urlConnection.getHeaderField("Set-Cookie");
            if(cookie==null){
                postSuccess = false;
                return null;
            }else {
                cookie = cookie.substring(0, cookie.indexOf(';'));
            }
        }
        catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cookie;
    }

}
