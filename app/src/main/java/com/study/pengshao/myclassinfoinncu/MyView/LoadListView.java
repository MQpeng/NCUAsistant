package com.study.pengshao.myclassinfoinncu.MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.study.pengshao.myclassinfoinncu.R;
import com.study.pengshao.myclassinfoinncu.utils.GetSystemTime;

/**
 * Created by PengShao on 2016/8/29.
 */
public class LoadListView extends ListView implements AbsListView.OnScrollListener {
    private View mHeaderView; // 头布局
    private float downY; // 按下的y坐标
    private float moveY; // 移动后的y坐标
    private int mHeaderViewHeight; // 头布局高度
    public static final int PULL_TO_REFRESH = 0;// 下拉刷新
    public static final int RELEASE_REFRESH = 1;// 释放刷新
    public static final int REFRESHING = 2; // 刷新中
    private int currentState = PULL_TO_REFRESH; // 当前刷新模式
    private RotateAnimation rotateUpAnim; // 箭头向上动画
    private RotateAnimation rotateDownAnim; // 箭头向下动画
    private View mArrowView;		// 箭头布局
    private TextView mTitleText;	// 头布局标题
    private ProgressBar pb;			// 进度指示器
    private TextView mLastRefreshTime; // 最后刷新时间
    private OnLoadListViewRefreshListener mListener; // 刷新监听

    private View footView;
    private int lastVisibleItem;
    private int totalItemCount;
    private boolean isLoad;
    private OnLoadListener onLoadListener;
    private OnLoadListViewScrollListener onLoadListViewScrollListener = null;
    public LoadListView(Context context) {
        super(context);
        initView(context);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        footView = inflater.inflate(R.layout.list_view_foot_view,null);
        footView.findViewById(R.id.relative_add_new).setVisibility(View.INVISIBLE);
        this.addFooterView(footView);
        this.setOnScrollListener(this);

        initHeaderView();
        initAnimation();
    }

    private void initAnimation() {
        // 向上转, 围绕着自己的中心, 逆时针旋转0 -> -180.
        rotateUpAnim = new RotateAnimation(0f, -180f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateUpAnim.setDuration(300);
        rotateUpAnim.setFillAfter(true); // 动画停留在结束位置

        // 向下转, 围绕着自己的中心, 逆时针旋转 -180 -> -360
        rotateDownAnim = new RotateAnimation(-180f, -360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateDownAnim.setDuration(300);
        rotateDownAnim.setFillAfter(true); // 动画停留在结束位置
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {

        mHeaderView = View.inflate(getContext(), R.layout.list_view_head_view, null);
        mArrowView = mHeaderView.findViewById(R.id.iv_arrow);
        pb = (ProgressBar) mHeaderView.findViewById(R.id.pb);
        mTitleText = (TextView) mHeaderView.findViewById(R.id.tv_title);
        mLastRefreshTime = (TextView) mHeaderView.findViewById(R.id.tv_desc_last_refresh);


        // 提前手动测量宽高
        mHeaderView.measure(0, 0);// 按照设置的规则测量

        mHeaderViewHeight = mHeaderView.getMeasuredHeight();

        // 设置内边距, 可以隐藏当前控件 , -自身高度
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

        // 在设置数据适配器之前执行添加 头布局/脚布局 的方法.
        addHeaderView(mHeaderView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 判断滑动距离, 给Header设置paddingTop
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                moveY = ev.getY();
                // 如果是正在刷新中, 就执行父类的处理
                if(currentState == REFRESHING){
                    return super.onTouchEvent(ev);
                }
                float offset = moveY - downY; // 移动的偏移量
                // 只有 偏移量>0, 并且当前第一个可见条目索引是0, 才放大头部
                if(offset > 0 && getFirstVisiblePosition() == 0){

//			        int paddingTop = -自身高度 + 偏移量
                    int paddingTop = (int) (- mHeaderViewHeight + offset);
                    if(paddingTop > 40){
                        paddingTop = 40;
                    }
                    mHeaderView.setPadding(0, paddingTop, 0, 0);

                    if(paddingTop >= 0 && currentState != RELEASE_REFRESH){// 头布局完全显示
                        // 切换成释放刷新模式
                        currentState = RELEASE_REFRESH;
                        updateHeader(); // 根据最新的状态值更新头布局内容
                    }else if(paddingTop < 0 && currentState != PULL_TO_REFRESH){ // 头布局不完全显示
                        // 切换成下拉刷新模式
                        currentState = PULL_TO_REFRESH;
                        updateHeader(); // 根据最新的状态值更新头布局内容
                    }
                    //事实发现并不需要下面的语句
//                    return true; // 当前事件被我们处理并消费
                }
                break;
            case MotionEvent.ACTION_UP:

                // 根据刚刚设置状态
                if(currentState == PULL_TO_REFRESH){
//			- paddingTop < 0 不完全显示, 恢复
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
                }else if(currentState == RELEASE_REFRESH){
//			- paddingTop >= 0 完全显示, 执行正在刷新...
                    mHeaderView.setPadding(0, 0, 0, 0);
                    currentState = REFRESHING;
                    updateHeader();
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据状态更新头布局内容
     */
    private void updateHeader() {
        switch (currentState) {
            case PULL_TO_REFRESH: // 切换回下拉刷新
                // 做动画, 改标题
                mArrowView.startAnimation(rotateDownAnim);
                mTitleText.setText("下拉刷新");

                break;
            case RELEASE_REFRESH: // 切换成释放刷新
                // 做动画, 改标题
                mArrowView.startAnimation(rotateUpAnim);
                mTitleText.setText("释放刷新");

                break;
            case REFRESHING: // 刷新中...
                mArrowView.clearAnimation();
                mArrowView.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.VISIBLE);
                mTitleText.setText("正在刷新中...");

                if(mListener != null&&!isLoad){
                    mListener.onRefresh(); // 通知调用者, 让其到网络加载更多数据.
                }

                break;

            default:
                break;
        }
    }

    public interface OnLoadListViewRefreshListener{
        void onRefresh(); // 下拉刷新
    }

    public void setLoadListViewRefreshListener(OnLoadListViewRefreshListener mListener) {
        this.mListener = mListener;
    }

    public void onRefreshComplete() {
            // 下拉刷新
        currentState = PULL_TO_REFRESH;
        mTitleText.setText("下拉刷新"); // 切换文本
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏头布局
        pb.setVisibility(View.INVISIBLE);
        mArrowView.setVisibility(View.VISIBLE);
        String time = GetSystemTime.getTime();
        mLastRefreshTime.setText("最后刷新时间: " + time);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if(mHeaderView.getPaddingTop()==-mHeaderViewHeight&&lastVisibleItem==totalItemCount && scrollState == SCROLL_STATE_IDLE){
            //加载数据
            if(!isLoad){
                isLoad = true;
                footView.findViewById(R.id.relative_add_new).setVisibility(View.VISIBLE);
                onLoadListener.onLoad();
            }
        }

        if(onLoadListViewScrollListener!= null && !isLoad) {
            onLoadListViewScrollListener.inOnScrollStateChanged(scrollState);
        }
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(onLoadListViewScrollListener!= null) {
            onLoadListViewScrollListener.inOnScroll(firstVisibleItem,visibleItemCount);
        }
        lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;

    }

    public void setOnLoadListViewScrollListener(OnLoadListViewScrollListener onLoadListViewScrollListener) {
        this.onLoadListViewScrollListener = onLoadListViewScrollListener;
    }

    public interface OnLoadListViewScrollListener{
        public void inOnScrollStateChanged(int scrollState);
        public void inOnScroll(int firstVisibleItem, int visibleItemCount);
    }

    public void completeLoad(){
        isLoad = false;
        footView.findViewById(R.id.relative_add_new).setVisibility(View.INVISIBLE);
    }

    public void setOnLoadListener(OnLoadListener onLoadListener){
        this.onLoadListener = onLoadListener;
    }
    public interface OnLoadListener{
        public void onLoad();
    }
}
