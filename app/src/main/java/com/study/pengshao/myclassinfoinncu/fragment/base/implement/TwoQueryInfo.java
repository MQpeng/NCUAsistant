package com.study.pengshao.myclassinfoinncu.fragment.base.implement;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.study.pengshao.myclassinfoinncu.HostTestActivity;
import com.study.pengshao.myclassinfoinncu.MyView.ViewPagerIndicator;
import com.study.pengshao.myclassinfoinncu.R;
import com.study.pengshao.myclassinfoinncu.adapter.ContentAdapter;
import com.study.pengshao.myclassinfoinncu.fragment.base.BasePager;
import com.study.pengshao.myclassinfoinncu.fragment.base.mark.implement.MarkPager;
import com.study.pengshao.myclassinfoinncu.fragment.base.mark.implement.PowerPager;
import com.study.pengshao.myclassinfoinncu.fragment.base.mark.implement.EnglishPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwoQueryInfo extends BasePager {

    private ViewPager mViewPager;
    private ViewPagerIndicator mIndicator;
    private List<BasePager> mPagers;
    private List<String> titles;
    public TwoQueryInfo(HostTestActivity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.two_layout,null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_test_mark);
        mIndicator = (ViewPagerIndicator) view.findViewById(R.id.vp_indicator);
        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<BasePager>();
        // 添加五个标签页
        mPagers.add(new MarkPager(mActivity));
        mPagers.add(new PowerPager(mActivity));
        mPagers.add(new EnglishPager(mActivity));
        mViewPager.setAdapter(new ContentAdapter(mPagers));

        titles = Arrays.asList("成绩查询","电费查询","四六级");
        mIndicator.setIndicatorItemView(titles);
        mIndicator.setViewPager(mViewPager,0);
        mIndicator.highLightTextView(0);
    }

}