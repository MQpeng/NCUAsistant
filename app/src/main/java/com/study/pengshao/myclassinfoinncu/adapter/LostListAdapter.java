package com.study.pengshao.myclassinfoinncu.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.pengshao.myclassinfoinncu.MyView.ItemClickDialog;
import com.study.pengshao.myclassinfoinncu.MyView.LoadListView;
import com.study.pengshao.myclassinfoinncu.R;
import com.study.pengshao.myclassinfoinncu.data_class.LostData;
import com.study.pengshao.myclassinfoinncu.utils.LoadImageFromUrl;
import com.study.pengshao.myclassinfoinncu.utils.NetWorkUtil;

import java.util.List;

/**
 * Created by PengShao on 2016/9/24.
 */
public class LostListAdapter extends BaseAdapter implements LoadListView.OnLoadListViewScrollListener, AdapterView.OnItemClickListener, ItemClickDialog.ItemClickDialogButtonClickListener {
    private List<LostData> data;
    private Context context;
    private LayoutInflater layoutInflater;
    private LoadImageFromUrl loadImageFromUrl;

    public static String[] URLS;
    private int mStart;
    private boolean isFirstLoadListView;
    private LoadListView mLoadListView;
    private ItemClickDialog itemClickDialog;

    public LostListAdapter(Context context, List<LostData> data, LoadListView listView) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        URLS = new String[data.size()];
        for (int i = 0;i<data.size();i++){
            URLS[i] = data.get(i).getLostImageUrlPath();
        }
        loadImageFromUrl = new LoadImageFromUrl(listView);
        isFirstLoadListView = true;
        mLoadListView = listView;
        mLoadListView.setOnLoadListViewScrollListener(this);
        mLoadListView.setOnItemClickListener(this);
    }

    public void onDateChange(List<LostData> data) {
        if(this.data.size() != data.size()){
            URLS = new String[data.size()];
            for (int i = 0;i<data.size();i++){
                URLS[i] = data.get(i).getLostImageUrlPath();
            }
            this.data = data;
            this.notifyDataSetChanged();
        }else{
            Toast.makeText(context,"啊噢~没有更多数据了",Toast.LENGTH_SHORT).show();
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

        LostData tempData = data.get(position);
        viewHolder.lostName.setText(tempData.getLostName());
        viewHolder.lostContent.setText(tempData.getLostContent());
        viewHolder.lostPosition.setText("丢失线索："+tempData.getLostPosition());
        viewHolder.lostTime.setText(tempData.getLostTime());

        ImageView imageView = viewHolder.lostImage;
        String url = tempData.getLostImageUrlPath();
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
        LostData lostData = data.get(position - 1);
        itemClickDialog = new ItemClickDialog(context);
        itemClickDialog.show();
        itemClickDialog.setName(lostData.getLostName());
        itemClickDialog.setContent(lostData.getLostContent());
        itemClickDialog.setPhone(lostData.getLostPhone());
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

    /*@Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE){
            //加载可见项
            if (NetWorkUtil.isNetworkConnected(context)) {
                loadImageFromUrl.loadImageFromStartToEnd(mStart,mEnd);
            }

        }else {
            //停止所有加载任务
            loadImageFromUrl.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;

        //该方法会多次调用visibleItemCount初始为0 ，跳过这个过程，固判断是否大于0
        if(isFirstLoadListView && visibleItemCount > 0){
            if (NetWorkUtil.isNetworkConnected(context)) {
                isFirstLoadListView = false;
                loadImageFromUrl.loadImageFromStartToEnd(mStart,mEnd);
            }
        }
    }*/

    class ViewHolder{
        TextView lostName;
        TextView lostContent;
        TextView lostPosition;
        TextView lostTime;
        ImageView lostImage;
    }
}
