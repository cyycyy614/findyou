package com.findyou.findyoueverywhere.controls;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.constant.ApiConst;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.HeaderAndFooterRecyclerViewAdapter;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * Created by Administrator on 2018/1/11 0011.
 */

public class MyRecyclerView extends LinearLayout {
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptr_layout;
    @BindView(R.id.recyclerViewFinal)
    RecyclerViewFinal recyclerViewFinal;

    @BindView(R.id.view_root)
    LinearLayout view_root;
    @BindView(R.id.view_empty)
    EmptyView view_empty;

    public interface RecyclerViewCallback{
        public void onRefresh();
        public void onLoadMore();
        public void onClickItem(RecyclerView.ViewHolder holder, int position);
    }

    RecyclerViewCallback callback;
    View loadMoreView;
    View mEmptyView;

    public MyRecyclerView(Context context) {
        super(context);
        init();
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //加载View
        View view = inflate(getContext(), R.layout.ptr_controls_recyclerview, this);
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
        recyclerViewFinal.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //发起加载更多请求
                if(callback != null){
                    callback.onLoadMore();
                    recyclerViewFinal.onLoadMoreComplete();//完成加载更多
                }
            }
        });
        recyclerViewFinal.setOnItemClickListener(new HeaderAndFooterRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                if(callback != null){
                    callback.onClickItem(holder, position);
                }
            }
        });
    }

    public void setHasLoadMore(int count){
        if (recyclerViewFinal != null) {
            if (count < ApiConst.PAGE_SIZE) {
                recyclerViewFinal.setHasLoadMore(false);
            } else {
                recyclerViewFinal.setHasLoadMore(true);
            }
        }
    }

    public void addHeaderView(View view){
        if(recyclerViewFinal != null){
            recyclerViewFinal.addHeaderView(view);
        }
    }

    public void setAdapter(RecyclerViewFinal.Adapter adapter) {
        if(recyclerViewFinal != null) {
            recyclerViewFinal.setAdapter(adapter);
        }
    }

    public void setCallback(RecyclerViewCallback callback){
        this.callback = callback;
    }

    public void setLoadMoreView(View view){
        loadMoreView = view;
        if(loadMoreView != null){
//            AVLoadMoreView avLoadMoreView = LoadMoreStyle.getAVLoadMoreViewFactory(view.getContext());
//            avLoadMoreView.setIndicatorColor(view.getResources().getColor(R.color.colorPrimary));
//            avLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallPulse);
//            if(recyclerViewFinal != null){
//                recyclerViewFinal.setLoadMoreView(avLoadMoreView);
//            }
        }
    }

    public RecyclerViewFinal getRecyclerViewFinal(){
        return recyclerViewFinal;
    }

    public void setEmptyView(boolean isShow){
        if (view_empty != null) {
            if (isShow) {
                view_empty.setVisibility(VISIBLE);
            } else {
                view_empty.setVisibility(GONE);
            }
        }
    }

    public void scrollToPosition(int n){
        recyclerViewFinal.scrollToPosition(n);
    }

    public void smoothScrollBy(int distance, int duration){
        recyclerViewFinal.smoothScrollBy(distance, duration);
    }
}
