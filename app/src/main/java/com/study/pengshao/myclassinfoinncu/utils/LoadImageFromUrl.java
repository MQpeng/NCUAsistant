package com.study.pengshao.myclassinfoinncu.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.study.pengshao.myclassinfoinncu.R;
import com.study.pengshao.myclassinfoinncu.adapter.LostListAdapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by PengShao on 2016/9/23.
 */



public class LoadImageFromUrl{

    private LruCache<String , Bitmap> mCache;
    private ListView mListView;
    private Set<GetImageByAsyncTak> mTask;

    public LoadImageFromUrl(ListView listView){
        mListView = listView;
        mTask = new HashSet<>();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mCache = new LruCache<String , Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次进入缓存时调用
                return value.getByteCount();
            }
        };
    }

    public void addBitmapToCache(String url,Bitmap bitmap){
        if(getBitmapFromCache(url) == null){
            mCache.put(url,bitmap);
        }
    }

    public Bitmap getBitmapFromCache(String url){
        return mCache.get(url);
    }

    /**
     * 加载所有从start开始的的图片
     * @param start
     *
     */
    public void loadImageFromStartToEnd(int start,String[]URLS){
        for (int i = start;i<URLS.length;i++){
            String url = URLS[i];
            Bitmap bitmap = getBitmapFromCache(url);
            if(bitmap == null){
                GetImageByAsyncTak task = new GetImageByAsyncTak(url);
                task.execute(url);
                mTask.add(task);
            }else {
                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
                if(imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    /**
     * 取消所有加载任务
     */
    public void cancelAllTasks() {
        if(mTask != null){
            for(GetImageByAsyncTak tempTask:mTask){
                tempTask.cancel(false);
            }
        }
    }

    public  void showImage(ImageView imageView,String url){
        Bitmap bitmap = getBitmapFromCache(url);
        if(bitmap == null){
            imageView.setImageResource(R.mipmap.image_loading);
        }else {
            imageView.setImageBitmap(bitmap);
        }
    }

    private class GetImageByAsyncTak extends AsyncTask<String , Void, Bitmap>{
        private String mUrl;
        public GetImageByAsyncTak(String url){
            mUrl = url;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);
            if(imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            //图片已经加载，移除task
            mTask.remove(this);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = getBitmapFromUrl(params[0]);
            if(bitmap != null){
                addBitmapToCache(params[0],bitmap);
            }
            return bitmap;
        }
    }

    public Bitmap getBitmapFromUrl(String path){
        Bitmap bitmap ;
        HttpURLConnection connection;
        InputStream is = null;
        BufferedInputStream bis = null;

        try {
            URL url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            is = connection.getInputStream();
            bis = new BufferedInputStream(is);
            bitmap = CompressImageUtil.getSmallBitmapFromStream(bis);
//            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

