package com.study.pengshao.myclassinfoinncu.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by PengShao on 2016/8/4.
 */
public class MyTextView extends TextView implements View.OnClickListener {

    private int courseInWeek;
    private int courseId = 0;
    private String teacherInfo;
    private String weekInfo;
    private String className;
    private String placeInfo;

    private int courseInWeek2;
    private int courseId2;
    private String teacherInfo2;
    private String weekInfo2;
    private String className2;
    private String placeInfo2;

    private int equalCount = 0;
    private String classInDoubleWeek = null;
    private boolean isDouble;
    private boolean classInDouble;

    public MyTextView(Context context) {
        super(context);
        this.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        TextViewClickDialog dialog = new TextViewClickDialog(getContext());
        dialog.show();
        String tempStr = getText().toString();
        tempStr = tempStr.substring(0,tempStr.indexOf("@"));
        if(tempStr.equals(className)) {
            dialog.setClassName("课程名称：" + className);
            dialog.setClassTeacher("老师：" + teacherInfo);
            dialog.setClassWeek("周数：" + weekInfo);
            dialog.setClassPlace("地点：" + placeInfo);
        }else if(tempStr.equals(className2)){
            dialog.setClassName("课程名称：" + className2);
            dialog.setClassTeacher("老师：" + teacherInfo2);
            dialog.setClassWeek("周数：" + weekInfo2);
            dialog.setClassPlace("地点：" + placeInfo2);
        }else {
            dialog.dismiss();
            Toast.makeText(getContext(),"没有什么好的信息",Toast.LENGTH_SHORT).show();
        }
    }

    public String getPlaceInfo() {
        return placeInfo;
    }

    public void setPlaceInfo(String placeInfo) {
        this.placeInfo = placeInfo;
    }

    public String getPlaceInfo2() {
        return placeInfo2;
    }

    public void setPlaceInfo2(String placeInfo2) {
        this.placeInfo2 = placeInfo2;
    }

    public boolean isClassInDouble() {
        return classInDouble;
    }

    public void setClassInDouble(boolean classInDouble) {
        this.classInDouble = classInDouble;
    }

    public String getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(String teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    public String getTeacherInfo2() {
        return teacherInfo2;
    }

    public void setTeacherInfo2(String teacherInfo2) {
        this.teacherInfo2 = teacherInfo2;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName2() {
        return className2;
    }

    public void setClassName2(String className2) {
        this.className2 = className2;
    }

    public int getCourseInWeek2() {
        return courseInWeek2;
    }

    public void setCourseInWeek2(int courseInWeek2) {
        this.courseInWeek2 = courseInWeek2;
    }

    public int getCourseId2() {
        return courseId2;
    }

    public void setCourseId2(int courseId2) {
        this.courseId2 = courseId2;
    }

    public String getWeekInfo2() {
        return weekInfo2;
    }

    public void setWeekInfo2(String weekInfo2) {
        this.weekInfo2 = weekInfo2;
    }

    public int getCourseInWeek() {
        return courseInWeek;
    }

    public boolean isDouble() {
        return isDouble;
    }

    public void setDouble(boolean aDouble) {
        isDouble = aDouble;
    }

    public void setCourseInWeek(int courseInWeek) {
        this.courseInWeek = courseInWeek;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getWeekInfo() {
        return weekInfo;
    }

    public void setWeekInfo(String weekInfo) {
        this.weekInfo = weekInfo;
    }

    public int getEqualCount() {
        return equalCount;
    }

    public void setEqualCount(int equalCount) {
        this.equalCount = equalCount;
    }

    public String getClassInDoubleWeek() {
        return classInDoubleWeek;
    }

    public void setClassInDoubleWeek(String classInDoubleWeek) {
        this.classInDoubleWeek = classInDoubleWeek;
    }


}
