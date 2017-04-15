package com.study.pengshao.myclassinfoinncu.fragment.base.mark.implement;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.study.pengshao.myclassinfoinncu.HostTestActivity;
import com.study.pengshao.myclassinfoinncu.R;
import com.study.pengshao.myclassinfoinncu.fragment.base.BasePager;
import com.study.pengshao.myclassinfoinncu.http_thread.ParseHTML;
import com.study.pengshao.myclassinfoinncu.utils.NetWorkUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by PengShao on 2016/8/15.
 */
public class EnglishPager extends BasePager{

    private EditText idCard;
    private EditText name;
    private TextView showResult;
    private Button startQuery;

    private String userIdCard;
    private String userName;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String urlPath = "http://www.weiweixiao.net/index.php?g=Wap&m=Cetuser&a=query";
    private String cookie;
    private boolean postSuccess;
    private String result;
    private Handler handler;
    private List<String> data;

    public EnglishPager(HostTestActivity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.english_query,null);
        idCard = (EditText) view.findViewById(R.id.english_query_et_id_card);
        name = (EditText) view.findViewById(R.id.english_query_et_name);
        startQuery = (Button) view.findViewById(R.id.english_query_bt);
        showResult = (TextView) view.findViewById(R.id.cet_data_tv);
        return view;
    }

    @Override
    public void initData() {
        sharedPreferences = mActivity.getSharedPreferences("NCU_CLASS", Context.MODE_PRIVATE);
        userIdCard = sharedPreferences.getString("CET_ID","");
        userName = sharedPreferences.getString("CET_NAME","");

        idCard.setText(userIdCard);
        name.setText(userName);

        startQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(name.getWindowToken(), 0); //强制隐藏键盘
                imm.hideSoftInputFromWindow(idCard.getWindowToken(), 0); //强制隐藏键盘
                editor = sharedPreferences.edit();
                userIdCard = idCard.getText().toString();
                userName = name.getText().toString();

                editor.putString("CET_ID",userIdCard);
                editor.putString("CET_NAME",userName);
                editor.commit();

                cookie = "";
                if(NetWorkUtil.isNetworkConnected(mActivity)) {
                    if(!userIdCard.isEmpty()&&!userName.isEmpty()) {
                        Toast.makeText(mActivity,"请稍后···",Toast.LENGTH_SHORT).show();
                        queryCet(userIdCard, userName);
                    }else {
                        Toast.makeText(mActivity,"准考证或姓名不能为空···",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(mActivity,"网络似乎断开了···",Toast.LENGTH_SHORT).show();
                }
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                StringBuffer sb = new StringBuffer();
                if(msg.arg1 != -1&&msg.obj!=null){
                    Toast.makeText(mActivity,"更新成功···",Toast.LENGTH_SHORT).show();
                    data = (List<String>) msg.obj;
                    for (int i = 0;i<data.size();i++) {
                        sb.append(data.get(i));
                        if(i!=data.size()-1){
                            sb.append("\n\n");
                        }else {
                            sb.append("\n");
                        }
                    }
                    sb.insert(sb.indexOf("听"),"\n\n");
                    sb.insert(sb.indexOf("写"),"\n\n");
                    showResult.setText(sb.toString());
                    showResult.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(mActivity,"请检查准考证号或姓名···",Toast.LENGTH_SHORT).show();
                }

            }
        };
    }
    private void queryCet(final String userIdCard, final String userName) {

        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);

                    conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    conn.setRequestProperty("Cookie",cookie);

                    String data = "zkzh="+userIdCard+"&xm="+userName+"&__hash__=5d0ec56e0162c6a93a49e9644c9e3c8a_57c4ee8219afc25131d7f0b660fefbc8";

                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(data.getBytes());
                    outputStream.flush();
                    outputStream.close();
                    cookie = conn.getHeaderField("Set-Cookie");

                    conn.connect();
                    int codePost = conn.getResponseCode();

                    if(codePost == 200) {
                        postSuccess = true;
                        if(cookie.isEmpty()){
                            postSuccess = false;
                        }else {
                            cookie = cookie.substring(0, cookie.indexOf(';'));
                        }
                    }else{
                        postSuccess = false;
                    }
                    InputStream is = null;
                    Message message = new Message();
                    if(postSuccess){
                        is = conn.getInputStream();
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(is,"utf-8"));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                        is.close();
                        reader.close();
                        result = sb.toString();

                        List<String> keys = new ArrayList<String>();
                        keys.add("table");
                        keys.add("tr");
                        keys.add("th");
                        keys.add("td");
                        List<String> list = ParseHTML.parseCETInfoFromUrlData(result,keys,0,false);
                        message.obj = list;
                        handler.sendMessage(message);
                    }else {
                        message.arg1 = -1;
                        handler.sendMessage(message);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
