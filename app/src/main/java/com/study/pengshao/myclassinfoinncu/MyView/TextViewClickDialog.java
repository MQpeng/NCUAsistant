package com.study.pengshao.myclassinfoinncu.MyView;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.study.pengshao.myclassinfoinncu.R;

/**
 * Created by PengShao on 2016/10/3.
 */
public class TextViewClickDialog extends AlertDialog{

    private TextView className,classTeacher,classWeek,classPlace;
    protected TextViewClickDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_text_view_click);
        className = (TextView) findViewById(R.id.dialog_class_name);
        classTeacher = (TextView) findViewById(R.id.dialog_class_teacher);
        classWeek = (TextView) findViewById(R.id.dialog_class_weeks);
        classPlace = (TextView) findViewById(R.id.dialog_class_place);
    }

    public void setClassPlace(String  str) {
        this.classPlace.setText(str);
    }

    public void setClassName(String  str) {
        this.className.setText(str);
    }

    public void setClassTeacher(String  str) {
        this.classTeacher.setText(str);
    }

    public void setClassWeek(String  str) {
        this.classWeek.setText(str);
    }
}
