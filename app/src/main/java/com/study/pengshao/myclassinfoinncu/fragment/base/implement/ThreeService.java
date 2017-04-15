package com.study.pengshao.myclassinfoinncu.fragment.base.implement;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.pengshao.myclassinfoinncu.Config;
import com.study.pengshao.myclassinfoinncu.HostTestActivity;
import com.study.pengshao.myclassinfoinncu.MyView.MyWebView;
import com.study.pengshao.myclassinfoinncu.R;
import com.study.pengshao.myclassinfoinncu.data_class.ImageUrlData;
import com.study.pengshao.myclassinfoinncu.fragment.base.BasePager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by PengShao on 2016/8/6.
 */
public class ThreeService extends BasePager implements View.OnClickListener {
    private MyWebView mWebView;
    private Button back;
    private TextView textView;
    private String urlPath = "http://mp.weixin.qq.com/mp/homepage?__biz=MzIxNTMyNDU1Mg==&hid=1&sn=b3bd0d720d4b35677d40672234d4efb8#wechat_redirect";
    private String str = "关注微信公众号：昌大脱单";

    private ImageView mErrorFrame;
    public ThreeService(HostTestActivity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.three_layout,null);
        mWebView = (MyWebView) view.findViewById(R.id.web_view);
        back = (Button) view.findViewById(R.id.back_web_view);
        textView = (TextView) view.findViewById(R.id.url_text_view);
        mErrorFrame = (ImageView) view.findViewById(R.id.web_error_view);
        return view;
    }


    @Override
    public void initData() {

        BmobQuery<ImageUrlData> queryLoadUrl = new BmobQuery<>();
        queryLoadUrl.getObject(Config.URL_ID, new QueryListener<ImageUrlData>() {
            @Override
            public void done(ImageUrlData imageUrlData, BmobException e) {
                if(e == null){
                    urlPath = imageUrlData.getUrlPath();
                    str = imageUrlData.getOtherStr();
                }
                setWebViewLoadUrl(urlPath,str);
            }
        });

        BmobQuery<ImageUrlData> queryFlashImage = new BmobQuery<>();
        queryFlashImage.getObject(Config.FLASH_ID, new QueryListener<ImageUrlData>() {
            @Override
            public void done(ImageUrlData imageUrlData, BmobException e) {
                if(e == null){
                    urlPath = imageUrlData.getUrlPath();
                    Boolean isLoadImage = imageUrlData.getLoadImage();
                    if(isLoadImage) {
                        storeImageFromUrl(urlPath);
                    }
                }
            }
        });
        back.setOnClickListener(this);
    }
    private void storeImageFromUrl(final String path){
        new Thread(){
            @Override
            public void run() {
                InputStream in = null;
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    in = conn.getInputStream();
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                File file = new File(mActivity.getFilesDir().getPath()+"/flash.jpg");
                if(file.exists()){
                    file.delete();
                }
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println(file.toString());
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    byte[] b = new byte[2*1024];
                    int len;
                    if(out != null){
                        while((len=in.read(b))!=-1){
                            out.write(b,0,len);
                        }
                    }
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }.start();

    }
    private void setWebViewLoadUrl(String urlPath,String str){
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        String databasePath = mActivity.getApplicationContext()
                .getDir("database", Context.MODE_PRIVATE).getPath();
        mWebView.getSettings().setDatabasePath(databasePath);
        mWebView.getSettings().setAppCacheEnabled(true);
        String appCaceDir =mActivity.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        mWebView.getSettings().setAppCachePath(appCaceDir);
        mWebView.setWebViewClient(new mWebViewClient());
        mWebView.loadUrl(urlPath);
        textView.setTextColor(Color.parseColor("#FF01AE07"));
        textView.setText(str);
    }

    public class mWebViewClient extends WebViewClient{

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mErrorFrame.setVisibility(View.VISIBLE);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_web_view:
                if(mWebView.canGoBack()){
                    mWebView.goBack();
                    if(mErrorFrame.getVisibility()==View.VISIBLE){
                        mErrorFrame.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }
}
