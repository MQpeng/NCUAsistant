package com.study.pengshao.myclassinfoinncu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.study.pengshao.myclassinfoinncu.utils.NetWorkUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by PengShao on 2016/8/5.
 */
public class login extends Activity{
    private TextView userAccount;
    private TextView userPassword;
    private Button submit,showPassword;
    private boolean editTextShowPassword = false;
    private CheckBox checkBox;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private Handler handler;
    private static final String TAG = "login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userAccount = (TextView) findViewById(R.id.user_et);
        userPassword = (TextView) findViewById(R.id.password_et);
        submit = (Button) findViewById(R.id.login_bt);
        showPassword = (Button) findViewById(R.id.show_password);
        checkBox = (CheckBox) findViewById(R.id.memory);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextShowPassword = !editTextShowPassword;
                if(editTextShowPassword){
                    // 设置EditText文本为可见的
                    showPassword.setText("隐藏");
                    userPassword.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());
                }else{
                    showPassword.setText("显示");
                    userPassword.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                }
                // 切换后将EditText光标置于末尾
                CharSequence charSequence = userPassword.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText,charSequence.length());
                }
            }
        });
        sharedPreferences = getSharedPreferences(Config.SHARED_TAG,Context.MODE_PRIVATE);
        userAccount.setText(sharedPreferences.getString("userName",""));
        if(sharedPreferences.getBoolean("isSava",false)){
            userPassword.setText(sharedPreferences.getString("passWord",""));
        }

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1 == -1){
                    Toast.makeText(login.this,"请检查用户名和密码,如果都正确,学校教务系统估计又崩盘了!",Toast.LENGTH_SHORT).show();
                    setViewVisible(true);
                }else if(msg.arg1 == 1){
                    Toast.makeText(login.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    sharedPreferences = getSharedPreferences(Config.SHARED_TAG,Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userName",userAccount.getText().toString());
                    editor.putString("passWord", userPassword.getText().toString());
                    if(checkBox.isChecked()) {
                        editor.putBoolean("isSava", true);
                    }else {
                        editor.putBoolean("isSava", false);
                    }

                    editor.putBoolean("isFirstIn",false);
                    editor.commit();

                    Intent intent = new Intent(login.this,HostTestActivity.class);
                    progressBar.setVisibility(View.GONE);
                    startActivity(intent);
                    finish();
                }
            }
        };

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (NetWorkUtil.isNetworkConnected(login.this)) {
                        if (userAccount.getText().length() == 0) {
                            Toast.makeText(login.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (userPassword.getText().length() == 0) {
                            Toast.makeText(login.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        setViewVisible(false);

                        final String name = userAccount.getText().toString();
                        final String pass = userPassword.getText().toString();
                        new Thread() {
                            @Override
                            public void run() {
                                doPost(name, pass);
                            }
                        }.start();

                    } else {
                        setViewVisible(true);
                        Toast.makeText(login.this, "请检查网络是否连接", Toast.LENGTH_SHORT).show();
                    }

            }
        });

    }

    private void doPost(String userName,String passWord){

        String urlPath = "http://218.64.56.18/jsxsd/xk/LoginToXk";
        String encode = Config.parseEncode(userName,passWord);
        try {
            String data = "userAccount="+ URLEncoder.encode(userName, "UTF-8")
                    + "&userPassword="+URLEncoder.encode(passWord, "UTF-8")
                    + "&encoded="+URLEncoder.encode(encode, "UTF-8");
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);

            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            String cookie = null;
            urlConnection.setRequestProperty("Cookie",cookie);

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();

            cookie = urlConnection.getHeaderField("Set-Cookie");
            if(!cookie.isEmpty()){
                cookie = cookie.substring(0, cookie.indexOf(';'));
                sharedPreferences = getSharedPreferences(Config.SHARED_TAG, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cookie", cookie);
                editor.commit();
            }

            urlConnection.connect();
            int codePost = urlConnection.getResponseCode();
            Message message = new Message();
            if(codePost == 302) {
                message.arg1 = 1;
            }else{
                message.arg1 = -1;
            }
            handler.sendMessage(message);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            setViewVisible(true);
            Toast.makeText(login.this,"登陆失败，检查网络是否有用",Toast.LENGTH_SHORT).show();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            setViewVisible(true);
            Toast.makeText(login.this,"登陆失败，检查网络是否有用",Toast.LENGTH_SHORT).show();
            return;

        }
    }

    public void setViewVisible(boolean isVisible){
        if(isVisible){
            submit.setEnabled(true);
            userAccount.setEnabled(true);
            userPassword.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        }else {
            submit.setEnabled(false);
            userAccount.setEnabled(false);
            userPassword.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        }

    }

}
