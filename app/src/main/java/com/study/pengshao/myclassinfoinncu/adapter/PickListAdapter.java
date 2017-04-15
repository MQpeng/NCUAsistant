package com.study.pengshao.myclassinfoinncu.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.pengshao.myclassinfoinncu.MyView.ItemClickDialog;
import com.study.pengshao.myclassinfoinncu.MyView.LoadListView;
import com.study.pengshao.myclassinfoinncu.R;
import com.study.pengshao.myclassinfoinncu.data_class.LostData;
import com.study.pengshao.myclassinfoinncu.data_class.PickData;
import com.study.pengshao.myclassinfoinncu.utils.LoadImageFromUrl;
import com.study.pengshao.myclassinfoinncu.utils.NetWorkUtil;

import java.util.List;

/**
 * Created by PengShao on 2016/9/24.
 */
public class PickListAdapter extends BaseAdapter implements LoadListView.OnLoadListViewScrollListener, AdapterView.OnItemClickListener, ItemClickDialog.ItemClickDialogButtonClickListener {
    private List<PickData> data;
    private Context context;
    private LayoutInflater layoutInflater;
    private LoadImageFromUrl loadImageFromUrl;

    public static String[] URLS;
    private int mStart;
    private boolean isFirstLoadListView;
    private LoadListView mLoadListView;
    private ItemClickDialog itemClickDialog;

    public PickListAdapter(Context context, List<PickData> data, LoadListView listView) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        URLS = new String[data.size()];
        for (int i = 0;i<data.size();i++){
            URLS[i] = data.get(i).getPickImageUrl();
        }
        loadImageFromUrl = new LoadImageFromUrl(listView);
        isFirstLoadListView = true;
        mLoadListView = listView;
        mLoadListView.setOnLoadListViewScrollListener(this);
        mLoadListView.setOnItemClickListener(this);
    }

    public void onDateChange(List<PickData> data,boolean isQueryByKey) {
        if(isQueryByKey){
            URLS = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                URLS[i] = data.get(i).getPickImageUrl();
            }
            this.data = data;
            this.notifyDataSetChanged();
        }else {
            if (this.data.size() != data.size()) {
                URLS = new String[data.size()];
                for (int i = 0; i < data.size(); i++) {
                    URLS[i] = data.get(i).getPickImageUrl();
                }

                this.data = data;
                this.notifyDataSetChanged();
            } else {
                Toast.makeText(context, "啊噢~没有更多数据了", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.lost_layout_item_view,null);
            viewHolder.lostName = (TextView) convertView.findViewById(R.id.lost_name);
            viewHolder.lostContent = (TextView) convertView.findViewById(R.id.lost_content);
            viewHolder.lostPosition = (TextView) convertView.findViewById(R.id.lost_position);
            viewHolder.lostTime = (TextView) convertView.findViewById(R.id.lost_time);
            viewHolder.lostImage = (ImageView) convertView.findViewById(R.id.lost_image);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PickData tempData = data.get(position);
        viewHolder.lostName.setText(tempData.getPickName());
        viewHolder.lostContent.setText(tempData.getPickContent());
        viewHolder.lostPosition.setText("线索："+tempData.getPickPosition());
        viewHolder.lostTime.setText(tempData.getPickTime());

        ImageView imageView = viewHolder.lostImage;
        String url = tempData.getPickImageUrl();
        imageView.setTag(url);
        loadImageFromUrl.showImage(imageView,url);
        return convertView;
    }

    @Override
    public void inOnScrollStateChanged(int scrollState) {
        if(scrollState == 0){
            //加载可见项
            if (NetWorkUtil.isNetworkConnected(context)) {
                loadImageFromUrl.loadImageFromStartToEnd(mStart,URLS);
            }

        }else {
            //停止所有加载任务
            loadImageFromUrl.cancelAllTasks();
        }
    }

    @Override
    public void inOnScroll(int firstVisibleItem, int visibleItemCount) {
        mStart = firstVisibleItem;
        //该方法会多次调用visibleItemCount初始为0 ，跳过这个过程，固判断是否大于0
        if(isFirstLoadListView && visibleItemCount > 0){
            if (NetWorkUtil.isNetworkConnected(context)) {
                isFirstLoadListView = false;
                loadImageFromUrl.loadImageFromStartToEnd(mStart,URLS);
            }
        }
    }

    /**
     * 处理listView的点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //因为增加了头布局，因此 -1
        PickData lostData = data.get(position - 1);
        itemClickDialog = new ItemClickDialog(context);
        itemClickDialog.show();
        itemClickDialog.setName(lostData.getPickName());
        itemClickDialog.setContent(lostData.getPickContent());
        itemClickDialog.setPhone(lostData.getPickerPhone());
        itemClickDialog.setItemClickDialogButtonClickListener(this);
    }

    /**
     * itemClickDialog.setItemClickDialogButtonClickListener(this);
     * @param phoneNumber
     */
    @Override
    public void onClick(String phoneNumber) {
        Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));//跳转到拨号界面，同时传递电话号码
        context.startActivity(dialIntent);
    }

    class ViewHolder{
        TextView lostName;
        TextView lostContent;
        TextView lostPosition;
        TextView lostTime;
        ImageView lostImage;
    }
}
