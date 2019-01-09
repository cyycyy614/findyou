package com.findyou.findyoueverywhere.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * @author weber
 * @since 1.0
 * @version 1.0
 */
public class InterceptedRelativeLayout extends RelativeLayout {

    private OnInterceptedTouchListener mTouchListener;

    public InterceptedRelativeLayout(Context context, AttributeSet attrs) {
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
