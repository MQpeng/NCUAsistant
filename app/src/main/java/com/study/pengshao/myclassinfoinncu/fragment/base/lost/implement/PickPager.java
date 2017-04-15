package com.study.pengshao.myclassinfoinncu.fragment.base.lost.implement;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.study.pengshao.myclassinfoinncu.Config;
import com.study.pengshao.myclassinfoinncu.HostTestActivity;
import com.study.pengshao.myclassinfoinncu.MyView.LoadListView;
import com.study.pengshao.myclassinfoinncu.MyView.MySubmitDialog;
import com.study.pengshao.myclassinfoinncu.R;
import com.study.pengshao.myclassinfoinncu.adapter.PickListAdapter;
import com.study.pengshao.myclassinfoinncu.data_class.PickData;
import com.study.pengshao.myclassinfoinncu.fragment.base.BasePager;
import com.study.pengshao.myclassinfoinncu.utils.CompressImageUtil;
import com.study.pengshao.myclassinfoinncu.utils.NetWorkUtil;

import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by PengShao on 2016/9/29.
 */
public class PickPager extends BasePager implements View.OnClickListener, LoadListView.OnLoadListViewRefreshListener, LoadListView.OnLoadListener, HostTestActivity.OnPickListener {
    private LoadListView mListView;
    private ProgressBar headProgressBar;
    private PickListAdapter pickListAdapter;
    private Button submitData;

    private String imageFilePath;
    private MySubmitDialog mySubmitDialog;

    private String loadImageUrlPath = "";
    private int limitDataSum = 5;

    private EditText queryKey;
    private Button queryByKey;

    private RelativeLayout refreshData;
    public PickPager(HostTestActivity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pick_layout,null);
        mListView = (LoadListView) view.findViewById(R.id.pick_list_view);
        submitData = (Button) view.findViewById(R.id.submit_pick_data);
        headProgressBar = (ProgressBar) view.findViewById(R.id.pick_layout_progress);
        queryKey = (EditText) view.findViewById(R.id.query_et_key_pick_data);
        queryByKey = (Button) view.findViewById(R.id.query_bt_pick_data);
        refreshData = (RelativeLayout) view.findViewById(R.id.relative_layout_pick_refresh);
        return view;
    }

    @Override
    public void initData() {
        Bmob.initialize(mActivity, Config.APPLICATION_ID);
        getPickData(limitDataSum);
        submitData.setOnClickListener(this);
        mActivity.setOnPickListener(this);
        queryByKey.setOnClickListener(this);

        refreshData.setOnClickListener(this);
    }
    //给listView 搭建adapter
    private void showListView(List<PickData> list,boolean isQueryByKey) {
        if (pickListAdapter == null) {
            pickListAdapter = new PickListAdapter(mActivity, list, mListView);
            mListView.setAdapter(pickListAdapter);
            mListView.setLoadListViewRefreshListener(this);
            mListView.setOnLoadListener(this);
            headProgressBar.setVisibility(View.INVISIBLE);
        } else {
            pickListAdapter.onDateChange(list,isQueryByKey);
        }
    }
    private void getPickData(int limitDataSum) {
        BmobQuery<PickData> query = new BmobQuery<>();
        query.setLimit(limitDataSum);
        query.order("-createdAt");
        query.findObjects(new FindListener<PickData>() {
            @Override
            public void done(List<PickData> list, BmobException e) {
                if(headProgressBar.getVisibility() != View.INVISIBLE) {
                    headProgressBar.setVisibility(View.INVISIBLE);
                    headProgressBar.setVisibility(View.INVISIBLE);
                }
                if(e == null){
                    if(list!=null) {
                        if (refreshData.getVisibility() != View.GONE) {
                            refreshData.setVisibility(View.GONE);
                        }
                        showListView(list,false);
                    }else {
                        //没有数据
                        Toast.makeText(mActivity,"没有数据",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //获取数据失败
                    Toast.makeText(mActivity,"获取数据失败",Toast.LENGTH_SHORT).show();

                    if(mListView.getChildCount()==0){
                        refreshData.setVisibility(View.VISIBLE);
                        headProgressBar.setVisibility(View.INVISIBLE);
                    }
                }
                mListView.onRefreshComplete();
                mListView.completeLoad();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.relative_layout_pick_refresh:
                getPickData(limitDataSum);
                break;
            //根据关键词查询数据
            case R.id.query_bt_pick_data:
                queryPickDataByKey();
                break;
            //创建上传数据的dialog
            case R.id.submit_pick_data:
                mySubmitDialog = new MySubmitDialog(mActivity);
                mySubmitDialog.setView(new EditText(mActivity));
                mySubmitDialog.setImageButtonClickListen(this);
                mySubmitDialog.setOnSubmitClickListener(this);
                mySubmitDialog.show();
                mySubmitDialog.setDialogTitle("雷 锋 帖");
                break;

            //上传照片的按钮点击事件处理
            case R.id.dialog_submit_ib_upload_image:
                upLoadImage();
                break;

            //上传数据的按钮点击事件处理
            case R.id.dialog_submit_lost_data:
                upLoadLostData();
                break;

            default:

                break;
        }
    }

    private void queryPickDataByKey() {
        headProgressBar.setVisibility(View.VISIBLE);
        String mQueryKey = queryKey.getText().toString();
        if(isStringEmpty(mQueryKey)){
            headProgressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(mActivity,"请输入关键词",Toast.LENGTH_SHORT).show();
            return;
        }
        BmobQuery<PickData> query = new BmobQuery<>();
        query.addWhereContains("pickName","校园卡");
//        query.addQueryKeys(mQueryKey);
//        query.setLimit(limitDataSum);
        query.order("-createdAt");
        query.findObjects(new FindListener<PickData>() {
            @Override
            public void done(List<PickData> list, BmobException e) {
                if(headProgressBar.getVisibility() != View.INVISIBLE) {
                    headProgressBar.setVisibility(View.INVISIBLE);
                }
                if(e == null){
                    if(list!=null && list.size()>0) {
                        showListView(list,true);
                    }else {
                        //没有数据
                        Toast.makeText(mActivity,"没有数据",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //获取数据失败
                    Toast.makeText(mActivity,"获取数据失败",Toast.LENGTH_SHORT).show();
                }
                mListView.onRefreshComplete();
                mListView.completeLoad();
            }
        });
    }

    private void upLoadLostData(){

        String lostName = mySubmitDialog.getLostName();
        String lostContent = mySubmitDialog.getLostContent();
        String lostPosition = mySubmitDialog.getLostPosition();
        String lostTime = mySubmitDialog.getLostTime();
        String lostPhone = mySubmitDialog.getLostPhone();

        if(isStringEmpty(lostName)){
            Toast.makeText(mActivity,"丢物品名称不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(isStringEmpty(lostContent)){
            Toast.makeText(mActivity,"要加上详细描述哟",Toast.LENGTH_SHORT).show();
            return;
        }
        if(isStringEmpty(lostPosition)){
            Toast.makeText(mActivity,"啊噢~又忘记填地点了吧",Toast.LENGTH_SHORT).show();
            return;
        }
        if(isStringEmpty(lostTime)){
            Toast.makeText(mActivity,"给加个时间吧",Toast.LENGTH_SHORT).show();
            return;
        }
        if(isStringEmpty(lostPhone)){
            Toast.makeText(mActivity,"要留下您的电话哟",Toast.LENGTH_SHORT).show();
            return;
        }
        if(isStringEmpty(loadImageUrlPath)){
            Toast.makeText(mActivity,"多少给个图片提示提示",Toast.LENGTH_SHORT).show();
            return;
        }
        if(NetWorkUtil.isNetworkConnected(mActivity)) {
            PickData mPickData = new PickData();
            mPickData.setPickName(lostName);
            mPickData.setPickContent(lostContent);
            mPickData.setPickPosition(lostPosition);
            mPickData.setPickTime(lostTime);
            mPickData.setPickerPhone(lostPhone);
            mPickData.setPickImageUrl(loadImageUrlPath);

            mPickData.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e == null){
                        Toast.makeText(mActivity,"发送成功",Toast.LENGTH_SHORT).show();
                        mySubmitDialog.dismiss();
                        headProgressBar.setVisibility(View.VISIBLE);
                        mListView.setSelection(0);
                        getPickData(++limitDataSum);
                    }else {
                        Toast.makeText(mActivity,"发送失败",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }else {
            Toast.makeText(mActivity,"请检查网络是否连接",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public boolean isStringEmpty(String str){
        return str.isEmpty();
    }

    private void upLoadImage(){
        // 拍照
        //设置图片的保存路径,作为全局变量
        String currentTime = String.valueOf(System.currentTimeMillis());

        imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/"+currentTime+".jpg";
        File temp = new File(imageFilePath);
        Uri imageFileUri = Uri.fromFile(temp);//获取文件的Uri
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//跳转到相机Activity
        it.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);//告诉相机拍摄完毕输出图片到指定的Uri
        mActivity.startActivityForResult(it, 103);
    }

    @Override
    public void pickDone() {
        mySubmitDialog.showProgressBar(true);
        final String path = CompressImageUtil.compressImage(imageFilePath,imageFilePath,20);
        File file = new File(path);
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    loadImageUrlPath = bmobFile.getFileUrl();
                    mySubmitDialog.showProgressBar(false);
                    mySubmitDialog.setImageView(path);

                }else{
                    Toast.makeText(mActivity,"上传图片失败,检查网络设置,请再次尝试",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onProgress(Integer value) {
                mySubmitDialog.setShowProgress(value);
            }
        });
    }

    @Override
    public void onRefresh() {
        getPickData(limitDataSum);
    }

    @Override
    public void onLoad() {
        limitDataSum += limitDataSum;
        getPickData(limitDataSum);
    }


}
