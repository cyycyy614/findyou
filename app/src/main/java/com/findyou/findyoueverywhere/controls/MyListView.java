package com.findyou.findyoueverywhere.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.findyou.findyoueverywhere.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.ListViewFinal;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;

/**
 * Created by Administrator on 2018/1/11 0011.
 */

public class MyListView extends LinearLayout {
    public interface ListViewCallback {
        public void onRefresh();
        public void onLoadMore();
        public void onClickItem(AdapterView<?> parent, View view, int position, long id);
    }

    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptr_layout;
    @BindView(R.id.listViewFinal)
    ListViewFinal listViewFinal;
    @BindView(R.id.view_empty)
    EmptyView view_empty;

    ListViewCallback callback;
    View loadMoreView;

    public MyListView(Context context) {
        super(context);
        init();
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //加载View
        View view = inflate(getContext(), R.layout.ptr_controls_listview, this);
        ButterKnife.bind(this, view);

        ptr_layout.disableWhenHorizontalMove(true); // ViewPager滑动冲突: disableWhenHorizontalMove() ms无用
        ptr_layout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //发起下拉刷新请求
                if(callback != null){
                    callback.onRefresh();
                    ptr_layout.onRefreshComplete();//完成下拉刷新
                }
            }
        });
        listViewFinal.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //发起加载更多请求
                if(callback != null){
                    callback.onLoadMore();
                    listViewFinal.onLoadMoreComplete();//完成加载更多
                }
            }
        });
        listViewFinal.setOnItemClickListener(new NoDoubleItemClickListener() {
            @Override
            protected void onNoDoubleItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(callback != null){
                    callback.onClickItem(parent, view, position, id);
                }
            }
        });
    }

    public void addHeaderView(View view){
        if(listViewFinal != null){
            listViewFinal.addHeaderView(view);
        }
    }

    public void setAdapter(ListAdapter adapter) {
        if(listViewFinal != null) {
            listViewFinal.setAdapter(adapter);
        }
    }

    public void setCallback(ListViewCallback callback){
        this.callback = callback;
    }

    public void setLoadMoreView(View view){
        loadMoreView = view;
        if(loadMoreView != null){
//            AVLoadMoreView avLoadMoreView = LoadMoreStyle.getAVLoadMoreViewFactory(view.getContext());
//            avLoadMoreView.setIndicatorColor(view.getResources().getColor(R.color.colorPrimary));
//            avLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallPulse);
//            if(listViewFinal != null){
//                listViewFinal.setLoadMoreView(avLoadMoreView);
//            }
        }
    }

    public void setEmptyView(boolean isShow) {
        if (view_empty != null) {
            if (isShow) {
                view_empty.setVisibility(VISIBLE);
            } else {
                view_empty.setVisibility(GONE);
            }
        }
    }

    public void smoothScrollToPosition(int position){
        listViewFinal.smoothScrollToPosition(position);
    }

    public void smoothScrollBy(int distance, int duration){
        listViewFinal.smoothScrollBy(distance, duration);
    }
}
