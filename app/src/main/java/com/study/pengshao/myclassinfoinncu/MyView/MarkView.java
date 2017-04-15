package com.study.pengshao.myclassinfoinncu.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import com.study.pengshao.myclassinfoinncu.data_class.MarkData;
import com.study.pengshao.myclassinfoinncu.utils.TranslateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PengShao on 2016/8/7.
 */
public class MarkView extends View{
    private float width;
    private float height;
    private float sectionWidth;
    private float sectionHeight;
    private Paint mPaint;
    private Paint textPaint;

    private String className;
    private String MarkInfo;
    private String classInfo;

    private List<MarkData> datas;
    private Context context;
    private float textSize;
    private static final String TAG = "MarkView";

    public MarkView(Context context) {
        super(context);
        init(context);
    }

    public MarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context){
        this.context = context;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF4BB4F5);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);

        textPaint = new Paint();
        textPaint.setColor(0x88000000);
        textPaint.setAntiAlias(true);
        textSize = TranslateUtil.sp2px(context,16);
        textPaint.setTextSize(textSize);
        datas = new ArrayList<MarkData>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!datas.isEmpty()) {

            //画横线
            for (float i = 0; i < 26; i++) {
                canvas.drawLine(0, i * sectionHeight, width, i * sectionHeight, mPaint);
            }
            //画竖线
            for (float i = 2; i < 5; i++) {
                canvas.drawLine(i * sectionWidth - TranslateUtil.px2dip(context,3) , 0,
                        i * sectionWidth - TranslateUtil.px2dip(context,3), height, mPaint);
            }

            for (int i = 0; i < datas.size(); i++) {
                className = datas.get(i).className;
                MarkInfo = datas.get(i).markInfo;
                classInfo = datas.get(i).classInfo;

                if(isNumeric(MarkInfo)){
                    if(Float.parseFloat(MarkInfo)<60f) {
                        textPaint.setColor(0xFFFF0000);
                    }else {
                        textPaint.setColor(0x88000000);
                    }
                }
                canvas.drawText(MarkInfo, (float) 5 * sectionWidth / 2f - textPaint.measureText(MarkInfo) / 2, (i + 1) * sectionHeight - sectionHeight/4, textPaint);

                int m = 1;
                canvas.drawText(classInfo, (float) 7 * sectionWidth / 2f - textPaint.measureText(classInfo) / 2, (i + 1) * sectionHeight - sectionHeight/4, textPaint);
                while((sectionWidth - textPaint.measureText(className) / 2)<0){
                    textSize = TranslateUtil.sp2px(context,16 - m);
                    textPaint.setTextSize(textSize);
                    m+=2;
                }
                canvas.drawText(className, sectionWidth - textPaint.measureText(className) / 2, (i + 1) * sectionHeight - sectionHeight/4, textPaint);
                textPaint.setTextSize(TranslateUtil.sp2px(context,16));
            }
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = getScreenWidth();
        sectionWidth = width / 4f;
        sectionHeight = width / 8f;
        height = sectionHeight * 24f;

        setMeasuredDimension((int) width,(int) height);
    }

    public int getScreenWidth() {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("^[-+]?[0-9]+(\\.[0-9]+)?$");
        Matcher isNum = pattern.matcher(str);
        if( isNum.matches() ) {
            return true;
        }
        return false;
    }

    public List<MarkData> getDatas() {
        return datas;
    }

    public void setDatas(List<MarkData> datas) {
        this.datas = datas;
    }
}
