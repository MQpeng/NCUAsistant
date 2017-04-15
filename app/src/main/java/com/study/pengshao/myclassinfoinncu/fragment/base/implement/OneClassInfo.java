package com.study.pengshao.myclassinfoinncu.fragment.base.implement;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.study.pengshao.myclassinfoinncu.Config;
import com.study.pengshao.myclassinfoinncu.HostTestActivity;
import com.study.pengshao.myclassinfoinncu.MyView.MyTextView;
import com.study.pengshao.myclassinfoinncu.MyView.ViewGroupView;
import com.study.pengshao.myclassinfoinncu.R;
import com.study.pengshao.myclassinfoinncu.data_class.AppVersionData;
import com.study.pengshao.myclassinfoinncu.data_class.ClassInfo;
import com.study.pengshao.myclassinfoinncu.data_class.UserData;
import com.study.pengshao.myclassinfoinncu.fragment.base.BasePager;
import com.study.pengshao.myclassinfoinncu.http_thread.ClassInfoThread;
import com.study.pengshao.myclassinfoinncu.login;
import com.study.pengshao.myclassinfoinncu.utils.BitmapWithStringUtil;
import com.study.pengshao.myclassinfoinncu.utils.NetWorkUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by PengShao on 2016/8/6.
 */
public class OneClassInfo extends BasePager implements View.OnClickListener, HostTestActivity.OnChangeBgListener {
    private String userAccount;
    private String userPassword;
    private Handler handler;
    private List<List<com.study.pengshao.myclassinfoinncu.data_class.ClassInfo>> data;

    private int[] resourceId = new int[]{R.drawable.bg_shape,
            R.drawable.bg_shape1,R.drawable.bg_shape2,R.drawable.bg_shape3,
            R.drawable.bg_shape4,R.drawable.bg_shape5,R.drawable.bg_shape6,
            R.drawable.bg_shape7,R.drawable.bg_shape8,R.drawable.bg_shape9,R.drawable.bg_shape10};
    private int colorId = 0;
    private Map<String,Integer> recordColor;
    private String[] arr = new String[]{
            "第1周","第2周","第3周","第4周","第5周","第6周","第7周","第8周",
            "第9周","第10周","第11周","第12周","第13周","第14周","第15周","第16周",
            "第17周","第18周","第19周","第20周"
    };
    private Random random = new Random();

    private int countNumber = 0;

    private Button changeBg;
    private ImageView backgroundChange;
    private Bitmap bmp = null;
    private Uri imageUri;

    private Spinner weekChoose;
    private Spinner yearChoose;
    private ViewGroupView layout;

    private int todayWeekNum = 16;
    private Calendar calendar;
    private int YEAR;
    private int MONTH;
    private int DAY;
    private int weekNum;
    private List<String> years = new ArrayList<String>();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private TextView mon,tues,wed,thur,fri,sat,sun;

    public OneClassInfo(HostTestActivity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.one_class_info_layout,null);
        changeBg = (Button) view.findViewById(R.id.changeBackgroundImage);
        backgroundChange = (ImageView) view.findViewById(R.id.one_change_image);
        layout = (ViewGroupView) view.findViewById(R.id.class_item_vg);
        weekChoose = (Spinner) view.findViewById(R.id.spinner_choose_week);
        yearChoose = (Spinner) view.findViewById(R.id.spinner_choose_year);

        mon = (TextView) view.findViewById(R.id.monday);
        tues = (TextView) view.findViewById(R.id.tuesday);
        wed = (TextView) view.findViewById(R.id.wednesday);
        thur = (TextView) view.findViewById(R.id.thursday);
        fri = (TextView) view.findViewById(R.id.friday);
        sat = (TextView) view.findViewById(R.id.saturday);
        sun = (TextView) view.findViewById(R.id.sunday);
        recordColor = new HashMap<String,Integer>();
        return view;
    }
    //更换背景按钮响应
    private void upDataDialog(final AppVersionData appVersionData){

        Dialog alertDialog = new AlertDialog.Builder(mActivity).
                setTitle("有新版本可下载").
                setMessage(appVersionData.getNewContent()).
                setPositiveButton("立即下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        String downloadPath = appVersionData.getUrlPath();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(downloadPath));
                        mActivity.startActivity(intent);
                        dialog.dismiss();
                    }
                }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        boolean isForce = appVersionData.getForce();
                        if(!isForce){
                            dialog.dismiss();
                        }else {
                            String downloadPath = appVersionData.getUrlPath();
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(downloadPath));
                            mActivity.startActivity(intent);
                            dialog.dismiss();
                        }
                    }
                }).create();
        alertDialog.show();
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        mActivity.startActivityForResult(Intent.createChooser(intent, "选择图片"), 101);
        mActivity.setChangeBgListener(this);
    }
    //相册返回
    @Override
    public void changeDone(Intent data) {
        //选择图片
        imageUri = data.getData();
        showBackground(backgroundChange,imageUri);
    }

    private void showBackground(ImageView imageView,Uri imageUri){
        ContentResolver cr = mActivity.getContentResolver();
        try {
            if(bmp != null)//如果不释放的话，不断取图片，将会内存不够
                bmp.recycle();
            InputStream in = cr.openInputStream(imageUri);
            if(in!=null) {
                bmp = BitmapFactory.decodeStream(in);
            }else {
                return;
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        imageView.setImageBitmap(bmp);

        if(sharedPreferences == null||editor == null){
            sharedPreferences = mActivity.getSharedPreferences(Config.SHARED_TAG, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        editor.putString("BackgroundBitmap",BitmapWithStringUtil.convertIconToString(bmp));
        editor.commit();
    }

    @Override
    public void initData() {
        //版本检测更新
        BmobQuery<AppVersionData> queryAppVersion = new BmobQuery<>();
        queryAppVersion.getObject(Config.VERSION_ID, new QueryListener<AppVersionData>() {
            @Override
            public void done(AppVersionData appVersionData, BmobException e) {
                if(e == null){
                    String versionNum = appVersionData.getVersionNum();
                    if(Integer.parseInt(versionNum) > Config.APP_VERSION){
                        upDataDialog(appVersionData);
                    }
                }
            }
        });

        changeBg.setOnClickListener(this);
        sharedPreferences = mActivity.getSharedPreferences(Config.SHARED_TAG, Context.MODE_PRIVATE);
        userAccount = sharedPreferences.getString("userName","");
        userPassword = sharedPreferences.getString("passWord","");
        editor = sharedPreferences.edit();
        if(userAccount.isEmpty()||userPassword.isEmpty()){
            Intent intent = new Intent(mActivity,login.class);
            mActivity.startActivity(intent);
            mActivity.finish();
            return;
        }
        String bitmapString  = sharedPreferences.getString("BackgroundBitmap",null);
        if(bitmapString!=null){
            bmp =BitmapWithStringUtil.convertStringToIcon(bitmapString);
            backgroundChange.setImageBitmap(bmp);
        }
        calendar = Calendar.getInstance();
        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH) + 1;

        if(years.isEmpty()) {
            String lastYear = (YEAR-1)+"";
            String year = YEAR+"";
            String nextYear = (YEAR+1)+"";
            years.add(lastYear + "-" + year + "-" + "1");
            years.add(lastYear + "-" + year + "-" + "2");
            years.add(year + "-" + nextYear + "-" + "1");
            years.add(year + "-" + nextYear + "-" + "2");
        }
        ArrayAdapter<String> yearArrayAdapter =
                new ArrayAdapter<String>(mActivity, R.layout.spinner_item, years);
        yearChoose.setAdapter(yearArrayAdapter);
        if(MONTH  >= 7) {
            yearChoose.setSelection(2);
        }else{
            yearChoose.setSelection(1);
        }

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
            if(msg.arg1 == -1){
                Toast.makeText(mActivity,"获取数据失败···",Toast.LENGTH_SHORT).show();
                editor.putBoolean("isFirstIn",false);
                editor.commit();
                return;
            }
            data = (List<List<com.study.pengshao.myclassinfoinncu.data_class.ClassInfo>>) msg.obj;
            if(countNumber!=0) {
                layout.removeAllViews();
                countNumber = 0;
                display();
            }else {
                display();
            }
            String userId = sharedPreferences.getString("UserId",null);
            if(userId == null) {
                UserData tempData = new UserData();
                tempData.setUserName(userAccount);
                tempData.setUserPassword(userPassword);
                tempData.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            editor.putString("UserId", s);
                            editor.commit();
                        }
                    }
                });
            }

            }
        };
        yearChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String chooseYear = years.get(position);
                if (NetWorkUtil.isNetworkConnected(mActivity)) {
                    new ClassInfoThread(mActivity, handler,
                            userAccount, userPassword, chooseYear).start();
                }else {
                    if(sharedPreferences.getString("ClassResultKey","1").equals(userAccount)
                            && null != sharedPreferences.getString("ClassResult",null)
                            && sharedPreferences.getString("ClassYearChoose","1").equals(chooseYear)) {

                        new ClassInfoThread(mActivity, handler,
                                userAccount, userPassword, chooseYear).start();
                    }else {
                        Toast.makeText(mActivity, "请连接网络···", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        int lastDay = sharedPreferences.getInt("lastDay",-1);
        int lastWeek = sharedPreferences.getInt("lastWeek",-1);
        int curWeek = sharedPreferences.getInt("lastChoose",0);
        DAY = calendar.get(Calendar.DAY_OF_YEAR);
        weekNum = calendar.get(Calendar.DAY_OF_WEEK);

        switch (weekNum){
            case 1:
                sun.setBackgroundColor(0x88B94DF7);
                mon.setBackgroundColor(0x44B94DF7);
                break;
            case 2:
                mon.setBackgroundColor(0x88B94DF7);
                tues.setBackgroundColor(0x44B94DF7);
                break;
            case 3:
                tues.setBackgroundColor(0x88B94DF7);
                wed.setBackgroundColor(0x44B94DF7);
                break;
            case 4:
                wed.setBackgroundColor(0x88B94DF7);
                thur.setBackgroundColor(0x44B94DF7);
                break;
            case 5:
                thur.setBackgroundColor(0x88B94DF7);
                fri.setBackgroundColor(0x44B94DF7);
                break;
            case 6:
                fri.setBackgroundColor(0x88B94DF7);
                sat.setBackgroundColor(0x44B94DF7);
                break;
            case 7:
                sat.setBackgroundColor(0x88B94DF7);
                sun.setBackgroundColor(0x44B94DF7);
                break;
            default:
                break;
        }

        if((lastDay!=-1 && lastWeek!=-1)&&(DAY - lastDay >= 7)){
            curWeek += (DAY - lastDay - (weekNum - lastWeek))/7;
        }else if(weekNum>1){
            if(weekNum < lastWeek) {
                curWeek += (DAY - lastDay - (weekNum - lastWeek)) / 7;
            }else if(lastWeek == 1){
                curWeek++;
            }
        }

        ArrayAdapter<String> weekArrayAdapter =
                new ArrayAdapter<String>(mActivity, R.layout.spinner_item, arr);
        weekChoose.setAdapter(weekArrayAdapter);
        weekChoose.setSelection(curWeek % arr.length);
        weekChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                todayWeekNum = position + 1;
                removeAllViewGroup();
                editor.putInt("lastChoose",position);
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        editor.putInt("lastDay",DAY);
        editor.putInt("lastWeek",weekNum);
        editor.commit();

    }
    private void removeAllViewGroup(){
        if(countNumber!=0) {
            layout.removeAllViews();
            countNumber = 0;
            display();
        }
    }

    private void display() {
        if(data.isEmpty()){
            Toast.makeText(mActivity,"获取数据失败，请检查网络设置",Toast.LENGTH_LONG).show();
            return;
        }

        for(int i =0 ;i<data.size();i++){
            List<ClassInfo> data2 = data.get(i);
            for(int j = 0;j<data2.size();j++){

                ClassInfo classInfo = data2.get(j);
                String className = classInfo.getClassName();
                if(className.length()<4){
                    continue;
                }
                String tempStr = className.substring(className.indexOf(" "));
                className = className.substring(0,className.indexOf(" "));

                int tempInt = tempStr.indexOf(")");
                if((tempInt + 1)!=tempStr.length()) {
                    tempStr = tempStr.substring(tempInt + 1);
                }else{

                }

                MyTextView myTextView = new MyTextView(mActivity);
                myTextView.setPadding(5,5,5,5);
                myTextView.setTextSize(10);
                myTextView.setTextColor(Color.WHITE);
                myTextView.setGravity(Gravity.CENTER);

                myTextView.setWeekInfo(classInfo.getMap().get("周次(节次)"));
                myTextView.setTeacherInfo(classInfo.getMap().get("老师"));
                myTextView.setPlaceInfo(classInfo.getMap().get("教室"));
                myTextView.setClassName(className);

                boolean check = isClassInToday(classInfo.getMap().get("周次(节次)"));
                if(check){
                    if(classInfo.getMap()!=null) {
                        if (classInfo.getMap().containsKey("教室")) {
                            if (!classInfo.getMap().get("教室").isEmpty()) {
                                myTextView.setText(myTextView.getClassName() + "@" + classInfo.getMap().get("教室"));
                            } else {
                                myTextView.setText(myTextView.getClassName() + "@暂无教室");
                            }
                        } else {
                            myTextView.setText(myTextView.getClassName() + "@暂无教室");
                        }
                    }

                    //保存图片颜色信息，使同课程的颜色相同
                    if(recordColor.containsKey(myTextView.getText().toString())){
                        int n = recordColor.get(myTextView.getText().toString());
                        myTextView.setBackgroundResource(resourceId[n]);
                    }else{
                        colorId++;
                        colorId %= resourceId.length;
                        myTextView.setTextColor(0xFFFFFFFF);
                        myTextView.setBackgroundResource(resourceId[colorId]);
                        recordColor.put(myTextView.getText().toString(),colorId);
                    }

                }else {
                    myTextView.setBackgroundResource(R.drawable.bg_shape_noclick);
                    myTextView.setTextColor(0xAA656666);
                    if(classInfo.getMap()!=null) {
                        if (classInfo.getMap().containsKey("教室")) {
                            if (!classInfo.getMap().get("教室").isEmpty()) {
                                myTextView.setText(myTextView.getClassName() + "@" + classInfo.getMap().get("教室"));
                            } else {
                                myTextView.setText(myTextView.getClassName() + "@暂无教室");
                            }
                        } else {
                            myTextView.setText(myTextView.getClassName() + "@暂无教室");
                        }
                    }
                }

                if(tempStr.contains("(")){
                    String tempStr2 = classInfo.getClassName();
                    for(int m = 0;m<classInfo.getMap().size()+2;m++){
                        if(!tempStr2.contains(" ")){
                            break;
                        }
                        tempStr2 = tempStr2.substring(tempStr2.indexOf(" ")+1);
                    }
                    myTextView.setDouble(true);
                    if(!tempStr2.contains(" ")&&tempStr2.contains("(")&&tempStr2.contains(")")){
                        myTextView.setClassName2(tempStr2.substring(tempStr2.indexOf("(")+1, tempStr2.indexOf(")")));
                    }else {
                        myTextView.setClassName2(tempStr2.substring(0, tempStr2.indexOf(" ")));
                    }
                    if(classInfo.getMap2()!=null) {
                        if (classInfo.getMap2().containsKey("周次(节次)")) {
                            myTextView.setWeekInfo2(classInfo.getMap2().get("周次(节次)"));
                        }
                        if (classInfo.getMap2().containsKey("老师")) {
                            myTextView.setTeacherInfo2(classInfo.getMap2().get("老师"));
                        }
                        if (classInfo.getMap2().containsKey("教室")) {
                            myTextView.setPlaceInfo2(classInfo.getMap2().get("教室"));
                        }

                        boolean check2 = isClassInToday(classInfo.getMap2().get("周次(节次)"));
                        if (check2&&!check) {
                            myTextView.setTextColor(0xFFFFFFFF);
                            if (classInfo.getMap2().containsKey("教室")) {
                                if (!classInfo.getMap2().get("教室").isEmpty()) {
                                    myTextView.setText(myTextView.getClassName2() + "@" + classInfo.getMap2().get("教室"));
                                } else {
                                    myTextView.setText(myTextView.getClassName2() + "@暂无教室");
                                }
                            } else {
                                myTextView.setText(myTextView.getClassName2() + "@暂无教室");
                            }

                            if (recordColor.containsKey(myTextView.getText().toString())) {
                                int n = recordColor.get(myTextView.getText().toString());
                                myTextView.setBackgroundResource(resourceId[n]);
                            } else {
                                colorId++;
                                colorId %= resourceId.length;
                                myTextView.setBackgroundResource(resourceId[colorId]);
                                recordColor.put(myTextView.getText().toString(), colorId);
                            }
                        }
                    }
                }

                String classPart = classInfo.getClassPart();
                classPart = classPart.substring(1);
                classPart = classPart.substring(0,classPart.indexOf("0"));
                if(classPart.equals("1")){
                    myTextView.setCourseId(1);
                }else if("3".equals(classPart)){
                    myTextView.setCourseId(2);
                }else if("5".equals(classPart)||"6".equals(classPart)){
                    myTextView.setCourseId(3);
                }else if("7".equals(classPart)||"8".equals(classPart)){
                    myTextView.setCourseId(4);
                }else if("91".equals(classPart)){
                    myTextView.setCourseId(5);
                }

                //2016学校对上课时间进行更改
                if(myTextView.getCourseId() == 0 && classPart.length()>=4){
                    classPart = classInfo.getClassPart();
                    classPart = classPart.substring(1,3);
                    if(classPart.equals("10")){
                        myTextView.setCourseId(1);
                    }else if("30".equals(classPart)){
                        myTextView.setCourseId(2);
                    }else if("60".equals(classPart)){
                        myTextView.setCourseId(3);
                    }else if("80".equals(classPart)){
                        myTextView.setCourseId(4);
                    }else if("11".equals(classPart)){
                        myTextView.setCourseId(5);
                    }
                }
                String temStr3 = classInfo.getId();
                myTextView.setCourseInWeek(Integer.parseInt(temStr3.substring(temStr3.indexOf("-")+1,temStr3.indexOf("-")+2)));

                layout.addView(myTextView);

                countNumber++;
            }
        }
    }

    private boolean isClassInToday(String weekInfo) {

        if(weekInfo.contains("单")){
            weekInfo = weekInfo.substring(0,weekInfo.indexOf("("));
            int left = Integer.parseInt(weekInfo.substring(0,weekInfo.indexOf("-")));
            int right = Integer.parseInt(weekInfo.substring(weekInfo.indexOf("-")+1));

            for(int i = left;i<=right;i++){
                if(i%2!=0){
                    if(i==todayWeekNum){
                        return true;
                    }
                }
            }
            return false;
        }else if(weekInfo.contains("双")){
            weekInfo = weekInfo.substring(0,weekInfo.indexOf("("));
            int left = Integer.parseInt(weekInfo.substring(0,weekInfo.indexOf("-")));
            int right = Integer.parseInt(weekInfo.substring(weekInfo.indexOf("-")+1));
            for(int i = left;i<=right;i++){
                if(i%2==0){
                    if(i==todayWeekNum){
                        return true;
                    }
                }
            }
            return false;
        }else{
            weekInfo = weekInfo.substring(0,weekInfo.indexOf("("));
            if(weekInfo.contains("-")&&weekInfo.contains(",")){
                int left;
                int right;
                int other = 0;

                left = Integer.parseInt(weekInfo.substring(0,weekInfo.indexOf("-")));
                right = Integer.parseInt(weekInfo.substring(weekInfo.indexOf("-")+1,weekInfo.indexOf(",")));

                if(todayWeekNum>=left&&todayWeekNum<=right) {
                    return true;
                }
                String tempStr = weekInfo.substring(weekInfo.indexOf(",")+1);
                if(tempStr.contains(",")){
                    String tempStr2 = tempStr.substring(0,tempStr.indexOf(","));
                    other = Integer.parseInt(tempStr2);
                    if(other != todayWeekNum){
                        if(tempStr.contains(",")) {
                            tempStr = tempStr.substring(tempStr.indexOf(",") + 1);
                            other = Integer.parseInt(tempStr);
                            if(other != todayWeekNum) {
                                return false;
                            }else {
                                return true;
                            }
                        }else {
                            return false;
                        }
                    }else {
                        return true;
                    }
                }else {
                    if(tempStr.contains("-")){
                        int left2 = Integer.parseInt(tempStr.substring(0,tempStr.indexOf("-")));
                        int right2 = Integer.parseInt(tempStr.substring(tempStr.indexOf("-")+1));

                        if(left2<todayWeekNum && todayWeekNum<right2){
                            return true;
                        }else{
                            return false;
                        }
                    }else {
                        other = Integer.parseInt(tempStr);
                        if (other != todayWeekNum) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                }

            }else if(!weekInfo.contains("-")&&!weekInfo.contains(",")){
                int left = Integer.parseInt(weekInfo);
                if(left != todayWeekNum){
                    return false;
                }else{
                    return true;
                }
            }else {
                if (weekInfo.contains("-")) {

                    int left;
                    int right;
                    left = Integer.parseInt(weekInfo.substring(0,weekInfo.indexOf("-")));
                    right = Integer.parseInt(weekInfo.substring(weekInfo.indexOf("-")+1));
                    if(todayWeekNum<left||todayWeekNum>right){
                        return false;
                    }else {
                        return true;
                    }
                }

                if (weekInfo.contains(",")) {
                    int other=Integer.parseInt(weekInfo.substring(0,weekInfo.indexOf(",")));
                    if(other == todayWeekNum){
                        return true;
                    }
                    String tempStr = weekInfo.substring(weekInfo.indexOf(",")+1);
                    if(tempStr.contains(",")){
                        String tempStr2 = tempStr.substring(0,tempStr.indexOf(","));
                        other = Integer.parseInt(tempStr2);
                        if(other != todayWeekNum){
                            if(tempStr.contains(",")) {
                                tempStr = tempStr.substring(tempStr.indexOf(",") + 1);
                                other = Integer.parseInt(tempStr);
                                if(other != todayWeekNum) {
                                    return false;
                                }else {
                                    return true;
                                }
                            }else {
                                return false;
                            }
                        }else {
                            return true;
                        }
                    }else {
                        other = Integer.parseInt(tempStr);
                        if(other != todayWeekNum){
                            return false;
                        }else {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
