package com.findyou.findyoueverywhere.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @author weber
 * @since 1.0
 * @version 1.0
 */
public class InterceptedLinearLayout extends LinearLayout {

    private OnInterceptedTouchListener mTouchListener;

    public InterceptedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mTouchListener != null) {
            return mTouchListener.onInterceptTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    public interface OnInterceptedTouchListener {
        boolean onInterceptTouchEvent(MotionEvent ev);
    }

    public void setOnInterceptTouchListener(OnInterceptedTouchListener touchListener) {
        this.mTouchListener = touchListener;
    }
}
