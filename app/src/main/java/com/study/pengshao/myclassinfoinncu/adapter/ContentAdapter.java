package com.study.pengshao.myclassinfoinncu.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.study.pengshao.myclassinfoinncu.fragment.base.BasePager;

import java.util.List;

/**
 * Created by PengShao on 2016/9/26.
 */
public class ContentAdapter extends PagerAdapter{
    private List<BasePager> mPagers;
    public ContentAdapter(List<BasePager> listPagers){
        mPagers = listPagers;
    }
    @Override
    public int getCount() {
        return mPagers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BasePager pager = mPagers.get(position);
        View view = pager.mRootView;
        container.addView(view);
        pager.initData();
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View)object);
    }

}
