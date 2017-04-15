package com.study.pengshao.myclassinfoinncu.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by PengShao on 2016/8/4.
 */
public class ClassInfoView extends View{

    private int horizontalLineNum = 8;
    private int verticalLineNum = 13;

    private Paint mPaint;
    private int width;
    private int height;
    private int sectionWidth;
    private int sectionHeight;
    private int lineWidth = 5;

    public ClassInfoView(Context context) {
        super(context);
        init();
    }

    public ClassInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClassInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFFEC870E);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(int i = 0;i<13;i++) {
            int j;
            for(j = 0;j<8;j++) {
                canvas.drawLine((float) (j*sectionWidth - lineWidth), (float)i*sectionHeight,
                        (float)(j*sectionWidth + lineWidth), (float)i*sectionHeight, mPaint);

                canvas.drawLine((float)j*sectionWidth, (float)(i*sectionHeight - lineWidth),
                        (float)j*sectionWidth, (float)(i*sectionHeight + lineWidth), mPaint);
            }
//            for(j = 0;j<8;j++) {
//                canvas.drawLine((float)j*sectionWidth, (float)(i*sectionHeight - lineWidth),
//                        (float)j*sectionWidth, (float)(i*sectionHeight + lineWidth), mPaint);
//            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = getScreenWidth();
        sectionWidth = width / (horizontalLineNum-1);
        sectionHeight = sectionWidth;
        height = sectionWidth * (verticalLineNum-1);
        setMeasuredDimension(width, height);
    }

    public int getScreenWidth() {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }
}
