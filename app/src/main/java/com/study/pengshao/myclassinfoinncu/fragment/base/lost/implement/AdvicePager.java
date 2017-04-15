package com.study.pengshao.myclassinfoinncu.fragment.base.lost.implement;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.study.pengshao.myclassinfoinncu.Config;
import com.study.pengshao.myclassinfoinncu.HostTestActivity;
import com.study.pengshao.myclassinfoinncu.R;
import com.study.pengshao.myclassinfoinncu.data_class.AdviceData;
import com.study.pengshao.myclassinfoinncu.data_class.LostData;
import com.study.pengshao.myclassinfoinncu.fragment.base.BasePager;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by PengShao on 2016/9/29.
 */
public class AdvicePager extends BasePager implements View.OnClickListener {

    private EditText name,phone,content;
    private Button submitAdvice;
    public AdvicePager(HostTestActivity mActivity) {
        super(mActivity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.advice_layout,null);

        name = (EditText) view.findViewById(R.id.advice_name);
        phone = (EditText) view.findViewById(R.id.advice_phone);
        content = (EditText) view.findViewById(R.id.advice_content);

        submitAdvice = (Button) view.findViewById(R.id.submit_advice);

        return view;
    }

    @Override
    public void initData() {
        submitAdvice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        submitAdvice.setFocusable(false);
        String nameStr = name.getText().toString();
        String phoneStr = phone.getText().toString();
        String contentStr = content.getText().toString();

        if(contentStr.isEmpty()){
            Toast.makeText(mActivity,"请输入您的建议",Toast.LENGTH_SHORT).show();
            return;
        }

        AdviceData mAdviceData = new AdviceData();
        mAdviceData.setName(nameStr);
        mAdviceData.setPhone(phoneStr);
        mAdviceData.setContent(contentStr);

        mAdviceData.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(mActivity,"提交成功，感谢您的来信",Toast.LENGTH_SHORT).show();
                    content.setText("");
                }else {
                    Toast.makeText(mActivity,"提交失败",Toast.LENGTH_SHORT).show();
                }
                submitAdvice.setFocusable(true);
            }
        });
    }
}
