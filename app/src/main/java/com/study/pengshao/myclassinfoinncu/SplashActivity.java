package com.study.pengshao.myclassinfoinncu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by PengShao on 2016/8/18.
 */
public class SplashActivity extends Activity{
    private SharedPreferences sharedPreferences;
    private String userName;
    private String userPassword;
    private boolean isFirstIn;
    private Handler handler;

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flash_activity);
        imageView = (ImageView) findViewById(R.id.splash_layout_image);
        File file = new File(getFilesDir().getPath()+"/flash.jpg");
        if (file.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            imageView.setImageBitmap(bitmap);
        }
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startNewActivity();
            }
        },3000);
    }

    private void startNewActivity(){
        sharedPreferences = getSharedPreferences("NCU_CLASS", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("userName","");
        userPassword = sharedPreferences.getString("passWord","");
        isFirstIn = sharedPreferences.getBoolean("isFirstIn",true);
        Intent intent;
        if(!isFirstIn&&userName!=null&&userPassword!=null){
            intent = new Intent(this,HostTestActivity.class);
        }else {
            intent = new Intent(this,login.class);
        }
        startActivity(intent);
        finish();
    }
}
