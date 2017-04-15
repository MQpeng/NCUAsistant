package com.study.pengshao.myclassinfoinncu.MyView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.study.pengshao.myclassinfoinncu.R;

import java.util.List;

/**
 * Created by PengShao on 2016/8/16.
 */
public class ViewPagerIndicator extends LinearLayout{

    private int visibleViews;
    private static final int DEFAULT_VISIBLE_COUNT = 3;
    private static final int NORMAL_COLOR = 0x88ffffff;
    private static final int HIGH_COLOR = 0xffffffff;

    private Paint mPaint;
    private Path mPath;
    private int mTriangleWidth;
    private int mTriangleHeight;
    private static final float RADIO_TRIANGLE_WIDTH = 1/6f;
    private final int MAX_TRIANGLE_WIDTH = (int) (getScreenWidth() / 3 *RADIO_TRIANGLE_WIDTH);
    private int mInitTranslationX;
    private int mTranslationX = 0;

    private ViewPager mViewPager;

    public interface PagerOnChangeListener{
        public void onPageScrolled(int position,
                                   float positionOffset, int positionOffsetPixels);
        public void onPageSelected(int position);
        public void onPageScrollStateChanged(int state);
    }
    private PagerOnChangeListener mListener;

    public void setOnPagerChangeListener(PagerOnChangeListener mListener) {
        this.mListener = mListener;
    }

    public void setViewPager(ViewPager ViewPager, int position) {
        mViewPager = ViewPager;
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position,positionOffset);
                if(mListener!=null){
                    mListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                highLightTextView(position);
                if(mListener!=null){
                    mListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(mListener!=null){
                    mListener.onPageScrollStateChanged(state);
                }
            }
        });
        mViewPager.setCurrentItem(position);
    }

    public void setVisibleViews(int visibleViews) {
        this.visibleViews = visibleViews;
    }

    public ViewPagerIndicator(Context context) {
        this(context,null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ViewPagerIndicator);
        visibleViews = typedArray.getInt(R.styleable.ViewPagerIndicator_visible_tab_count,
                DEFAULT_VISIBLE_COUNT);
        if(visibleViews<0){
            visibleViews = DEFAULT_VISIBLE_COUNT;
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#FFFFFFFF"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    /**
     * 布局加载结束后
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int counts = getChildCount();
        if(counts==0)
            return;
        for(int i = 0;i<counts;i++){
            View view = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.weight = 0;
            layoutParams.width = getScreenWidth() / visibleViews;
            view.setLayoutParams(layoutParams);
        }
        setItemClickEvent();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTranslationX+mTranslationX,getHeight());
        canvas.drawPath(mPath,mPaint);
        canvas.restore();

        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = (int) (w / visibleViews * RADIO_TRIANGLE_WIDTH);
        mTriangleWidth = Math.min(mTriangleWidth,MAX_TRIANGLE_WIDTH);
        mInitTranslationX = (w/visibleViews - mTriangleWidth)/2;
        mTriangleHeight = mTriangleWidth / 2;
        initTriangle();
    }

    private void initTriangle() {
        mPath = new Path();
        mPath.moveTo(0,0);
        mPath.lineTo(mTriangleWidth,0);
        mPath.lineTo(mTriangleWidth/2,-mTriangleHeight);
        mPath.close();
    }

    /**
     * 当titles的数量大于需要显示的数量时，将整个空间进行水平滑动
     * @param position
     * @param offset
     */
    public void scroll(int position,float offset){

        int tabWidth = getWidth() / visibleViews;
        mTranslationX = (int) (tabWidth*(offset + position));

        if(position >= (visibleViews - 2)&&offset>0&&getChildCount()>visibleViews) {
            if (visibleViews != 1) {
                this.scrollTo((position - (visibleViews - 2)) * tabWidth + (int)(tabWidth * offset), 0);
            }else {
                this.scrollTo(position  * tabWidth + (int)(tabWidth * offset), 0);
            }
        }
        //重绘控件
        invalidate();
    }

    /**
     * 根据titles为Indicator设置标题
     * @param titles
     */
    public void setIndicatorItemView(List<String> titles){

        if(titles!=null && titles.size()>0){
            for(String title:titles){
                addView(generalTextView(title));
            }
            setItemClickEvent();
        }
    }

    /**
     * 根据title创建TextView
     * @param title
     * @return
     */

    private View generalTextView(String title) {

        TextView textView = new TextView(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.width = getScreenWidth() / visibleViews;
        textView.setText(title);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        textView.setTextColor(NORMAL_COLOR);
        textView.setLayoutParams(layoutParams);

        return textView;
    }

    /**
     * 重置所有文字的颜色
     */

    private void resetTextViewColor(){
        int count = getChildCount();
        for(int i = 0;i<count;i++){
            View view = getChildAt(i);
            if(view instanceof TextView){
                ((TextView) view).setTextColor(NORMAL_COLOR);
            }
        }
    }
    /**
     * 设置高亮文字
     * @param position
     */
    public void highLightTextView(int position){
        resetTextViewColor();
        View view = getChildAt(position);
        if(view instanceof TextView){
            ((TextView) view).setTextColor(HIGH_COLOR);
        }
    }

    /**
     * 点击处理
     * @return
     */
    private void setItemClickEvent(){
        int count = getChildCount();

        for (int i = 0 ;i<count;i++){
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

    private int getScreenWidth() {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }
}
