package com.study.pengshao.myclassinfoinncu.fragment.base.lost.implement;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.study.pengshao.myclassinfoinncu.adapter.LostListAdapter;
import com.study.pengshao.myclassinfoinncu.data_class.LostData;
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
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by PengShao on 2016/9/26.
 */
public class LostPager extends BasePager implements View.OnClickListener, LoadListView.OnLoadListener, LoadListView.OnLoadListViewRefreshListener, HostTestActivity.OnLostListener {
    private LoadListView mListView;
    private ProgressBar headProgressBar;
    private LostListAdapter lostListAdapter;
    private Button submitData;

    private Bitmap bmp = null;
    private MySubmitDialog mySubmitDialog;

    private String loadImageUrlPath = "";
    private int limitDataSum = 5;

    private RelativeLayout refreshData;
    public LostPager(HostTestActivity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.lost_layout, null);
        mListView = (LoadListView) view.findViewById(R.id.lost_list_view);
        submitData = (Button) view.findViewById(R.id.submit_lost_data);
        headProgressBar = (ProgressBar) view.findViewById(R.id.lost_layout_progress);
        refreshData = (RelativeLayout) view.findViewById(R.id.relative_layout_lost_refresh);
        return view;
    }


    @Override
    public void initData() {
        Bmob.initialize(mActivity, Config.APPLICATION_ID);
        getLostData(limitDataSum);
        submitData.setOnClickListener(this);
        refreshData.setOnClickListener(this);
    }

    //给listView 搭建adapter
    private void showListView(List<LostData> list) {
        if (lostListAdapter == null) {
            lostListAdapter = new LostListAdapter(mActivity, list, mListView);
            mListView.setAdapter(lostListAdapter);
            mListView.setLoadListViewRefreshListener(this);
            mListView.setOnLoadListener(this);
            headProgressBar.setVisibility(View.INVISIBLE);
        } else {
            lostListAdapter.onDateChange(list);
        }
    }

    private void getLostData(int limitDataSum) {

        BmobQuery<LostData> query = new BmobQuery<>();
        query.setLimit(limitDataSum);
        query.order("-createdAt");
        query.findObjects(new FindListener<LostData>() {
            @Override
            public void done(List<LostData> list, BmobException e) {
                if (headProgressBar.getVisibility() != View.INVISIBLE) {
                    headProgressBar.setVisibility(View.INVISIBLE);
                }
                if (e == null) {
                    if (list != null) {
                        if (refreshData.getVisibility() != View.GONE) {
                            refreshData.setVisibility(View.GONE);
                            headProgressBar.setVisibility(View.INVISIBLE);
                        }
                        showListView(list);
                    } else {
                        //没有数据
                        Toast.makeText(mActivity, "没有数据", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //获取数据失败
                    Toast.makeText(mActivity, "获取数据失败", Toast.LENGTH_SHORT).show();
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
        switch (v.getId()) {
            //点击屏幕刷新
            case R.id.relative_layout_lost_refresh:
                getLostData(limitDataSum);
                break;

            //创建上传数据的dialog
            case R.id.submit_lost_data:
                mySubmitDialog = new MySubmitDialog(mActivity);
                mySubmitDialog.setView(new EditText(mActivity));
                mySubmitDialog.setImageButtonClickListen(this);
                mySubmitDialog.setOnSubmitClickListener(this);
                mySubmitDialog.show();
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

    private void upLoadLostData() {

        String lostName = mySubmitDialog.getLostName();
        String lostContent = mySubmitDialog.getLostContent();
        String lostPosition = mySubmitDialog.getLostPosition();
        String lostTime = mySubmitDialog.getLostTime();
        String lostPhone = mySubmitDialog.getLostPhone();

        if (isStringEmpty(lostName)) {
            Toast.makeText(mActivity, "丢物品名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isStringEmpty(lostContent)) {
            Toast.makeText(mActivity, "要加上详细描述哟", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isStringEmpty(lostPosition)) {
            Toast.makeText(mActivity, "啊噢~又忘记填地点了吧", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isStringEmpty(lostTime)) {
            Toast.makeText(mActivity, "给加个时间吧", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isStringEmpty(lostPhone)) {
            Toast.makeText(mActivity, "要留下您的电话哟", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isStringEmpty(loadImageUrlPath)) {
            Toast.makeText(mActivity, "多少给个图片提示提示", Toast.LENGTH_SHORT).show();
            return;
        }
        if (NetWorkUtil.isNetworkConnected(mActivity)) {
            LostData mLostData = new LostData();
            mLostData.setLostName(lostName);
            mLostData.setLostContent(lostContent);
            mLostData.setLostPosition(lostPosition);
            mLostData.setLostTime(lostTime);
            mLostData.setLostPhone(lostPhone);
            mLostData.setLostImageUrlPath(loadImageUrlPath);

            mLostData.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e == null){
                        Toast.makeText(mActivity,"发送成功",Toast.LENGTH_SHORT).show();
                        mySubmitDialog.dismiss();
                        headProgressBar.setVisibility(View.VISIBLE);
                        mListView.setSelection(0);
                        getLostData(++limitDataSum);
                    }else {
                        Toast.makeText(mActivity,"发送失败",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(mActivity, "请检查网络是否连接", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public boolean isStringEmpty(String str) {
        return str.isEmpty();
    }

    private void upLoadImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        mActivity.startActivityForResult(Intent.createChooser(intent, "选择图片"), 102);
        mActivity.setOnLostListener(this);
    }

    @Override
    public void lostDone(Intent data) {
        mySubmitDialog.showProgressBar(true);
        Uri imageUri = data.getData();
        File file = null;
        if(imageUri != null){
            file = CompressImageUtil.getFileFromMediaUri(mActivity,imageUri);
        }
        System.out.println(file.getPath().toString());
        final String path = CompressImageUtil.compressImage(file.getPath(), file.getPath(), 20);
        file = new File(path);
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    loadImageUrlPath = bmobFile.getFileUrl();
                    mySubmitDialog.showProgressBar(false);
                    mySubmitDialog.setImageView(path);
                } else {
                    Toast.makeText(mActivity, "上传图片失败,检查网络设置,请再次尝试", Toast.LENGTH_SHORT).show();
                    mySubmitDialog.showProgressBar(false);
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
        getLostData(limitDataSum);
    }

    @Override
    public void onLoad() {
        limitDataSum += limitDataSum;
        getLostData(limitDataSum);
    }


}
