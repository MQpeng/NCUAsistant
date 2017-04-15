package com.study.pengshao.myclassinfoinncu.http_thread;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.study.pengshao.myclassinfoinncu.data_class.ClassInfo;
import com.study.pengshao.myclassinfoinncu.data_class.UserData;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by PengShao on 2016/8/3.
 */
public class ClassInfoThread extends HttpPostThread{

    private String getInfoURL = "http://218.64.56.18/jsxsd/xskb/xskb_list.do";
    private String xnxq01id;
    private List<List<ClassInfo>> data;
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public ClassInfoThread(Context context, Handler handler,String userAccount, String userPassword,String xnxq01id) {
        super(context, userAccount, userPassword);
        this.handler = handler;
        this.xnxq01id = xnxq01id;
        sharedPreferences = context.getSharedPreferences("NCU_CLASS", Context.MODE_PRIVATE);
    }

    @Override
    public void run() {
        super.run();
        if(postSuccess) {

            String result;
            HttpDoGet httpDoGet = new HttpDoGet(cookie);
            String url = getInfoURL + "?xnxq01id=" + xnxq01id;
            result = httpDoGet.downloadURL(url);
            Message message = new Message();
            if(result==null){
                return;
            }
            handleResult(result);
            if(data==null){
                String key;
                result = sharedPreferences.getString("ClassResult",null);
                key = sharedPreferences.getString("ClassResultKey","1");
                if(result!=null && key.equals(userAccount)) {
                    handleResult(result);
                    message.obj = data;
                    handler.sendMessage(message);
                }else {
                    message.arg1 = -1;
                    handler.sendMessage(message);
                }
            }else {
                editor = sharedPreferences.edit();
                editor.putString("ClassResultKey",userAccount);
                editor.putString("ClassResult",result);
                editor.putString("ClassYearChoose",xnxq01id);
                editor.commit();
                message.obj = data;
            }
            handler.sendMessage(message);
        }else {
            String result;
            String key;
            result = sharedPreferences.getString("ClassResult",null);
            key = sharedPreferences.getString("ClassResultKey","1");
            if(result!=null && key.equals(userAccount)) {
                Message message = new Message();
                handleResult(result);
                message.obj = data;
                handler.sendMessage(message);
            }
        }
    }

    private void handleResult(String result){
        ParseHTML parseHTML = new ParseHTML();
        List<String> keys = new ArrayList<String>();
        keys.add("table");
        keys.add("tr");
        keys.add("th");
        keys.add("td");
        keys.add("div");
        keys.add("font");
        data = parseHTML.parseMyClassInfoFromUrlData(result, keys, 1, false);
    }

}
