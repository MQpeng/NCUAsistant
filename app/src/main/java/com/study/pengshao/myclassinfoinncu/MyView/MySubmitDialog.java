package com.study.pengshao.myclassinfoinncu.MyView;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.study.pengshao.myclassinfoinncu.R;
import com.study.pengshao.myclassinfoinncu.utils.ShowKeyBoard;

/**
 * Created by PengShao on 2016/9/1.
 */
public class MySubmitDialog extends AlertDialog {

    private EditText lostName,lostContent,lostPosition,lostTime,lostPhone;
    private ImageView submitShowImage;
    private ImageButton upLoadImage;
    private Button submitLostData;
    private TextView dialogTitle;

    private View.OnClickListener onClickListener;
    private View.OnClickListener imageButtonClickListen;

    private RelativeLayout progressLayout;
    private TextView showProgress;

    public MySubmitDialog(Context context) {
        super(context);

    }

    public MySubmitDialog(Context context, int themeResId) {
        super(context, themeResId);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_lost_dialog_layout);

        dialogTitle = (TextView) findViewById(R.id.dialog_submit_tv_lost_title);

        lostName = (EditText) findViewById(R.id.dialog_submit_et_lost_name);
        lostContent = (EditText) findViewById(R.id.dialog_submit_et_lost_content);
        lostPosition = (EditText) findViewById(R.id.dialog_submit_et_lost_position);
        lostTime = (EditText) findViewById(R.id.dialog_submit_et_lost_time);
        lostPhone = (EditText) findViewById(R.id.dialog_submit_tv_lost_phone);

        submitShowImage = (ImageView) findViewById(R.id.dialog_submit_iv_image_show);
        upLoadImage = (ImageButton) findViewById(R.id.dialog_submit_ib_upload_image);
        submitLostData = (Button) findViewById(R.id.dialog_submit_lost_data);

        progressLayout = (RelativeLayout) findViewById(R.id.layout_progress_bar);
        showProgress = (TextView) findViewById(R.id.progress_tv_show_progress);

        upLoadImage.setOnClickListener(imageButtonClickListen);
        submitLostData.setOnClickListener(onClickListener);
    }

    public void setDialogTitle(String str) {
        dialogTitle.setText(str);
    }

    public void setImageButtonClickListen(View.OnClickListener imageButtonClickListen) {
        this.imageButtonClickListen = imageButtonClickListen;
    }

    public void setOnSubmitClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setImageView(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        submitShowImage.setImageBitmap(bitmap);
    }

    public void showProgressBar(boolean isProgressShow){
        if(isProgressShow) {
            progressLayout.setVisibility(View.VISIBLE);
        }else {
            progressLayout.setVisibility(View.INVISIBLE);
            setShowProgress(0);
        }
    }

    public void setShowProgress(Integer str) {
        showProgress.setText(str+"%");
    }

    public String getLostPhone() {
        return lostPhone.getText().toString();
    }

    public String getLostName() {

        return lostName.getText().toString();
    }

    public String getLostContent() {
        return lostContent.getText().toString();
    }

    public String getLostPosition() {
        return lostPosition.getText().toString();
    }

    public String getLostTime() {
        return lostTime.getText().toString();
    }
}
