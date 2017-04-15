package com.study.pengshao.myclassinfoinncu.fragment.base;

import android.view.View;
import android.widget.FrameLayout;

import com.study.pengshao.myclassinfoinncu.HostTestActivity;
import com.study.pengshao.myclassinfoinncu.R;

/**
 * Created by PengShao on 2016/8/6.
 */
public class BasePager {
    public HostTestActivity mActivity;
    public View mRootView;

    public BasePager(HostTestActivity mActivity){
        this.mActivity = mActivity;
        this.mRootView = initView();
    }

    public View initView(){

        return null;
    }

    public void initData(){

    }
}
