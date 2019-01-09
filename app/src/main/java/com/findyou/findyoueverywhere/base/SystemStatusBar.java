package com.findyou.findyoueverywhere.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.utils.UIUtils;

public class SystemStatusBar {
    private static final int ITEM_SPACE = 10; //每个item之间的间隔距离
    private LinearLayout mStatusBar = null;
    private FrameLayout mTitleBar = null;
    private LinearLayout mBottomLine = null;
    private Context mContext;
    private FrameLayout mRootView;
    private Activity mActivity;

    public enum ViewAlign {
        Left, Center, Right
    }

    public SystemStatusBar(Activity activity) {
        mActivity = activity;

        mStatusBar = (LinearLayout) activity.findViewById(R.id.system_status_bar);
        if (mStatusBar == null) {
            return;
        }
        mStatusBar.setVisibility(View.VISIBLE);
        int statusHeight = UIUtils.getStatusHeight(mActivity);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusBar.getLayoutParams();
        params.height = statusHeight;
        mStatusBar.setLayoutParams(params);
    }

    public void showStatusBar(boolean isShow) {
        if (mStatusBar == null) {
            return;
        }
        if (isShow) {
            mStatusBar.setVisibility(View.VISIBLE);
        } else {
            mStatusBar.setVisibility(View.GONE);
        }
    }

    public void showTitleBar(boolean isShow) {
        if (mTitleBar == null) {
            //标题栏
            mTitleBar = (FrameLayout) mActivity.findViewById(R.id.system_title_bar);
        }
        if (mTitleBar == null) {
            return;
        }
        if (isShow) {
            mTitleBar.setVisibility(View.VISIBLE);
        } else {
            mTitleBar.setVisibility(View.GONE);
        }
    }

    public void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
            //v.setPadding(10, 10, 10, 10);
        }
    }

    public void setTitleBarHeight(int height) {
        mTitleBar.getLayoutParams().height = height;
    }

    public void setStatusBarBackground(String color) {
        if (mStatusBar != null) {
            mStatusBar.setBackgroundColor(Color.parseColor(color));
        }
    }

    public void setTitleBarBackgroundColor(String color) {
        if (mTitleBar != null) {
            mTitleBar.setBackgroundColor(Color.parseColor(color));
        }
    }

    //在状态栏添加功能按钮
    public void addView(View view, ViewAlign pos) {
        if (view == null) {
            return;
        }
        mTitleBar.addView(view);
    }
}

