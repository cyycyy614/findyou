package com.findyou.findyoueverywhere.controls.adapter;

import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.base.CommonCallback;

public class ViewPageChangeListener implements ViewPager.OnPageChangeListener {
    private ViewGroup viewGroup; // 指示点
    private int[] resid = new int[]{ R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused };
    private CommonCallback callback;

    public ViewPageChangeListener(ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
    }

    public void setCallback(CommonCallback callback){
        this.callback = callback;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageSelected(int pageIndex) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            ImageView view = (ImageView) viewGroup.getChildAt(i);
            if (i == pageIndex) {
                view.setImageResource(resid[1]);
            } else {
                view.setImageResource(resid[0]);
            }
        }
        if(this.callback != null){
            this.callback.apply(pageIndex);
        }
    }
}
