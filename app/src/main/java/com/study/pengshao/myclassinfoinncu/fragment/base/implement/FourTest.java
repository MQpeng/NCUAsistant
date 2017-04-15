package com.study.pengshao.myclassinfoinncu.fragment.base.implement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.study.pengshao.myclassinfoinncu.Config;
import com.study.pengshao.myclassinfoinncu.HostTestActivity;
import com.study.pengshao.myclassinfoinncu.MyView.MySubmitDialog;
import com.study.pengshao.myclassinfoinncu.MyView.ViewPagerIndicator;
import com.study.pengshao.myclassinfoinncu.R;
import com.study.pengshao.myclassinfoinncu.adapter.ContentAdapter;
import com.study.pengshao.myclassinfoinncu.adapter.LostListAdapter;
import com.study.pengshao.myclassinfoinncu.data_class.LostData;
import com.study.pengshao.myclassinfoinncu.fragment.base.BasePager;
import com.study.pengshao.myclassinfoinncu.fragment.base.lost.implement.AdvicePager;
import com.study.pengshao.myclassinfoinncu.fragment.base.lost.implement.LostPager;
import com.study.pengshao.myclassinfoinncu.fragment.base.lost.implement.PickPager;
import com.study.pengshao.myclassinfoinncu.fragment.base.mark.implement.EnglishPager;
import com.study.pengshao.myclassinfoinncu.fragment.base.mark.implement.MarkPager;
import com.study.pengshao.myclassinfoinncu.fragment.base.mark.implement.PowerPager;
import com.study.pengshao.myclassinfoinncu.utils.CompressImageUtil;
import com.study.pengshao.myclassinfoinncu.utils.NetWorkUtil;
import com.study.pengshao.myclassinfoinncu.utils.ShowKeyBoard;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by PengShao on 2016/9/24.
 */
public class FourTest extends BasePager {

    private ViewPager mViewPager;
    private ViewPagerIndicator mIndicator;
    private List<String> titles;
    private List<BasePager> mPagers;
    public FourTest(HostTestActivity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.four_test_layout,null);
        mViewPager = (ViewPager) view.findViewById(R.id.lost_vp_four);
        mIndicator = (ViewPagerIndicator) view.findViewById(R.id.lost_vp_indicator);
        return view;
    }

    @Override
    public void initData() {

        mPagers = new ArrayList<BasePager>();
        // 添加五个标签页
        mPagers.add(new LostPager(mActivity));
        mPagers.add(new PickPager(mActivity));
        mPagers.add(new AdvicePager(mActivity));
        mViewPager.setAdapter(new ContentAdapter(mPagers));

        titles = Arrays.asList("丢失物品","叫我雷锋","还没想好");
        mIndicator.setIndicatorItemView(titles);
        mIndicator.setViewPager(mViewPager,0);
        mIndicator.highLightTextView(0);

    }

}
