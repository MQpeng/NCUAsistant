package com.study.pengshao.myclassinfoinncu.fragment.base.mark.implement;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.study.pengshao.myclassinfoinncu.HostTestActivity;
import com.study.pengshao.myclassinfoinncu.MyView.MarkView;
import com.study.pengshao.myclassinfoinncu.R;
import com.study.pengshao.myclassinfoinncu.data_class.MarkData;
import com.study.pengshao.myclassinfoinncu.fragment.base.BasePager;
import com.study.pengshao.myclassinfoinncu.http_thread.HttpDoGet;
import com.study.pengshao.myclassinfoinncu.http_thread.HttpPostThread;
import com.study.pengshao.myclassinfoinncu.http_thread.ParseHTML;
import com.study.pengshao.myclassinfoinncu.utils.NetWorkUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by PengShao on 2016/8/15.
 */
public class MarkPager extends BasePager implements View.OnClickListener {
    private LinearLayout layout;
    private MarkView markView;
    private Spinner chooseYear;
    private List<MarkData> datas;
    private SharedPreferences sharedPreferences;
    private String userAccount;
    private String userPassword;
    private String baseURL = "http://218.64.56.18/jsxsd/kscj/cjcx_list?kksj=";
    private String URLpath = "";
    private Handler handler;

    private Calendar calendar;
    private int YEAR;
    private List<String> years = new ArrayList<String>();

    private Button startQuery;

    public MarkPager(HostTestActivity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.mark, null);
        layout = (LinearLayout) view.findViewById(R.id.mark_layout);
        chooseYear = (Spinner) view.findViewById(R.id.mark_year_choose);
        startQuery = (Button) view.findViewById(R.id.bt_mark_start_query);
        return view;
    }

    @Override
    public void initData() {
        startQuery.setOnClickListener(this);
        sharedPreferences = mActivity.getSharedPreferences("NCU_CLASS", Context.MODE_PRIVATE);
        userAccount = sharedPreferences.getString("userName","");
        userPassword = sharedPreferences.getString("passWord","");

        calendar = Calendar.getInstance();
        YEAR = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String lastYear = (YEAR - 1) + "";
        String year = YEAR + "";
        String lastLastYear = (YEAR - 2) + "";
        if (years.isEmpty()) {
            years.add(lastLastYear + "-" + lastYear + "-" + "1");
            years.add(lastLastYear + "-" + lastYear + "-" + "2");
            years.add(lastYear + "-" + year + "-" + "1");
            years.add(lastYear + "-" + year + "-" + "2");
        }

        ArrayAdapter<String> yearArrayAdapter =
                new ArrayAdapter<String>(mActivity, R.layout.spinner_item, years);
        chooseYear.setAdapter(yearArrayAdapter);
        chooseYear.setSelection(3);

        chooseYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                URLpath = baseURL + years.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                markView = new MarkView(mActivity);
                if (msg.obj.toString()==null && msg.arg1==-1) {
                    Toast.makeText(mActivity, "获取数据失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(mActivity, "更新成功", Toast.LENGTH_SHORT).show();
                markView.setDatas((List<MarkData>) msg.obj);
                if (layout.getChildCount() > 0) {
                    layout.removeAllViews();
                    layout.addView(markView);
                } else {
                    layout.addView(markView);
                }
            }
        };
    }

    private class markThread extends HttpPostThread {

        public markThread(Context context, String userAccount, String userPassword) {
            super(context, userAccount, userPassword);
        }

        @Override
        public void run() {
            super.run();
            if (postSuccess) {
                HttpDoGet httpDoGet = new HttpDoGet(cookie);
                String url = URLpath;
                String result = httpDoGet.downloadURL(url);
                List<String> keys = new ArrayList<>();
                keys.add("table");
                keys.add("tr");
                keys.add("td");

                Message message = new Message();
                datas = ParseHTML.parseMyMarkDataFromUrlData(result, keys, 1, false);
                if(datas==null){
                    message.arg1 = -1;
                }else {
                    message.obj = datas;
                }
                handler.sendMessage(message);
            }
        }

    }

    @Override
    public void onClick(View v) {
        if(NetWorkUtil.isNetworkConnected(mActivity)) {
            Toast.makeText(mActivity,"请稍后···",Toast.LENGTH_SHORT).show();
            new markThread(mActivity, userAccount, userPassword).start();
        }else {
            Toast.makeText(mActivity,"网络似乎断开了···",Toast.LENGTH_SHORT).show();
        }
    }

}
