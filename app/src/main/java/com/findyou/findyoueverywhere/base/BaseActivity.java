package com.findyou.findyoueverywhere.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {
    private int layoutid = -1;
    public SystemStatusBar toolBar;
    protected boolean isShowBack = true;
    protected boolean isShowStatusBar = true;
    protected boolean isShowTitleBar = true;
    private TextView tvTitle;
    private Context curActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //强制竖屏
        curActivity = this;
        layoutid = getLayoutId();
        if (layoutid > 0) {
            setContentView(layoutid);
        }
        ButterKnife.bind(this);

        toolBar = new SystemStatusBar(this);
        if (isShowStatusBar) {
            toolBar.showStatusBar(isShowStatusBar);
        }
        if (isShowTitleBar) {
            toolBar.showTitleBar(isShowTitleBar);
        }
        initTitle();
        initView();
    }

    public boolean isDestroy() {
         if(curActivity == null){
             return true;
         }else {
             return false;
         }
    }

    public void initView() {
    }

    protected int getLayoutId() {
        return layoutid;
    }

    public void setTitle(String title) {
        if (tvTitle == null) {
            return;
        }
        tvTitle.setText(title);
    }

    private void initTitle() {
        tvTitle = new TextView(this);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
        tvTitle.setMaxEms(15);
        tvTitle.setTextColor(Color.BLACK);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        curActivity = null;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //Finger touch screen event
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // get current focus,Generally it is EditText
            View view = getCurrentFocus();
            if (isShouldHideSoftKeyBoard(view, ev)) {
                hideSoftKeyBoard(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * Judge what situation hide the soft keyboard,click EditText view should show soft keyboard
     * @param v Incident event
     * @param event
     * @return
     */
    private boolean isShouldHideSoftKeyBoard(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] l = { 0, 0 };
            view.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top +view.getHeight(), right = left
                    + view.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // If click the EditText event ,ignore it
                return false;
            } else {
                return true;
            }
        }
        // if the focus is EditText,ignore it;
        return false;
    }

    /**
     * hide soft keyboard
     * @param token
     */
    private void hideSoftKeyBoard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
