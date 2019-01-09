package com.findyou.findyoueverywhere.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.findyou.findyoueverywhere.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/1 0001.
 */

public class EmptyView extends LinearLayout {
    public EmptyView(Context context) {
        super(context);
        init();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //加载View
        View view = inflate(getContext(), R.layout.controls_empty_view, this);
        ButterKnife.bind(this, view);
    }
}
