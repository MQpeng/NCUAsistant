package com.study.pengshao.myclassinfoinncu;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.study.pengshao.myclassinfoinncu.fragment.ContentFragment;

import cn.bmob.v3.update.BmobUpdateAgent;


/**
 * Created by PengShao on 2016/8/6.
 */
public class HostTestActivity extends Activity {

    private long exitTime;

    private OnLostListener onLostListener;
    private OnPickListener onPickListener;
    private OnChangeBgListener changeBgListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_fragment);
        initFragment();
    }

    public void setOnLostListener(OnLostListener onLostListener) {
        this.onLostListener = onLostListener;
    }

    public void setOnPickListener(OnPickListener onPickListener) {
        this.onPickListener = onPickListener;
    }

    public void setChangeBgListener(OnChangeBgListener changeBgListener) {
        this.changeBgListener = changeBgListener;
    }

    public interface OnLostListener{
        public void lostDone(Intent data);
    }
    public interface OnPickListener{
        public void pickDone();
    }
    public interface OnChangeBgListener{
        public void changeDone(Intent data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 101){
            if(resultCode == Activity.RESULT_OK && data!=null){
               changeBgListener.changeDone(data);
            }
        }else if(requestCode == 102){
            if(resultCode == Activity.RESULT_OK){
                onLostListener.lostDone(data);
            }
        }else if(requestCode == 103){
            if(resultCode == Activity.RESULT_OK){
                onPickListener.pickDone();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.base_fragment_layout,new ContentFragment());
        fragmentTransaction.commit();
    }
}
