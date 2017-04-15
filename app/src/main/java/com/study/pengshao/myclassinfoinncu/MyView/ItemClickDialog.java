package com.study.pengshao.myclassinfoinncu.MyView;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.study.pengshao.myclassinfoinncu.R;

/**
 * Created by PengShao on 2016/9/1.
 */
public class ItemClickDialog extends AlertDialog implements View.OnClickListener {

    private TextView name,content,phone;
    private Button contactPhone;
    private ItemClickDialogButtonClickListener itemClickDialogButtonClickListener;


    public ItemClickDialog(Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_click_dialog_view);
        name = (TextView) findViewById(R.id.item_click_name);
        content = (TextView) findViewById(R.id.item_click_tv_content);
        phone = (TextView) findViewById(R.id.item_click_tv_phone);
        contactPhone = (Button) findViewById(R.id.item_click_bt_contact);

        contactPhone.setOnClickListener(this);
    }

    public void setName(String str) {
        name.setText(str);
    }

    public void setContent(String str) {
        content.setText(str);
    }

    public void setPhone(String str) {
        phone.setText(str);
    }

    public void setItemClickDialogButtonClickListener(ItemClickDialogButtonClickListener itemClickDialogButtonClickListener) {
        this.itemClickDialogButtonClickListener = itemClickDialogButtonClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickDialogButtonClickListener.onClick(phone.getText().toString());
    }

    public interface ItemClickDialogButtonClickListener{
        public void onClick(String phoneNumber);
    }
}
