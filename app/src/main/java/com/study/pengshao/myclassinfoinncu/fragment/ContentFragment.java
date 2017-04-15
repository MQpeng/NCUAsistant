package com.study.pengshao.myclassinfoinncu.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.study.pengshao.myclassinfoinncu.Config;
import com.study.pengshao.myclassinfoinncu.MyView.NoScrollViewPager;
import com.study.pengshao.myclassinfoinncu.R;
import com.study.pengshao.myclassinfoinncu.data_class.ImageUrlData;
import com.study.pengshao.myclassinfoinncu.fragment.base.BasePager;
import com.study.pengshao.myclassinfoinncu.fragment.base.implement.FourTest;
import com.study.pengshao.myclassinfoinncu.fragment.base.implement.OneClassInfo;
import com.study.pengshao.myclassinfoinncu.fragment.base.implement.ThreeService;
import com.study.pengshao.myclassinfoinncu.fragment.base.implement.TwoQueryInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.update.BmobUpdateAgent;


/**
 * Created by PengShao on 2016/8/6.
 */
public class ContentFragment extends BaseFragment{
    private NoScrollViewPager mViewPager;
    private RadioGroup rgGroup;
    private List<BasePager>  mPagers;
    private List<Integer> record;

    private String urlPath;
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.host_main,null);
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.view_pager);
        rgGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        record = new ArrayList<Integer>();
        return view;
    }

    @Override
    protected void initData() {
        Bmob.initialize(mActivity, Config.APPLICATION_ID);

        mPagers = new ArrayList<BasePager>();
        // 添加五个标签页
        mPagers.add(new OneClassInfo(mActivity));
        mPagers.add(new TwoQueryInfo(mActivity));
        mPagers.add(new ThreeService(mActivity));
        mPagers.add(new FourTest(mActivity));

        mViewPager.setAdapter(new ContentAdapter());
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.class_rb:
                        mViewPager.setCurrentItem(0, false);// 参2:表示是否具有滑动动画
                        break;
                    case R.id.exam_rb:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.query_rb:
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.people_rb:
                        mViewPager.setCurrentItem(3, false);
                        break;
                    default:
                        break;
                }
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if(position!=0&&!record.contains(position)) {
                    record.add(position);
                    BasePager pager = mPagers.get(position);
                    pager.initData();
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagers.get(0).initData();

    }

    class ContentAdapter extends PagerAdapter {

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

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View)object);
        }
    }


}
