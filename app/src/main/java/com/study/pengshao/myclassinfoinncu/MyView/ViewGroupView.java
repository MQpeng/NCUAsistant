package com.study.pengshao.myclassinfoinncu.MyView;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by PengShao on 2016/8/4.
 */
public class ViewGroupView extends ViewGroup{

    private int horizontalLineNum = 8;
    private int verticalLineNum = 13;
    private int dividerWidth = 2;

    private int width;
    private int height;
    private int sectionWidth;
    private int sectionHeight;

    public ViewGroupView(Context context) {
        this(context,null);
    }

    public ViewGroupView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ViewGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        width = getScreenWidth();
        sectionWidth = width / (horizontalLineNum-1);
        sectionHeight = sectionWidth;
        height = sectionWidth * (verticalLineNum-1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();//获得子控件个数
        for (int i = 0; i < count; i++) {
            MyTextView child = (MyTextView) getChildAt(i);

            int courseInWeek = child.getCourseInWeek();//获得周几
            int courseId = child.getCourseId();
            int equalCount = 0;

            int left = (int) (sectionWidth * (courseInWeek-1)+dividerWidth);//计算左边的坐标
            int right = (int) (left + sectionWidth - 2*dividerWidth);//计算右边坐标
            int top = (int) (2*sectionHeight * (courseId-1)+dividerWidth);//计算顶部坐标
            int bottom = (int) (top + 2*sectionHeight- 2*dividerWidth);//计算底部坐标

            child.layout(left, top - (int)(2*sectionHeight*equalCount), right, bottom);

        }
    }

    public int getScreenWidth() {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }
}
