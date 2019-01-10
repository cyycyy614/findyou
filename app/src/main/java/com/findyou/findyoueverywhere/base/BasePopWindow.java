package com.findyou.findyoueverywhere.base;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.utils.UIUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import razerdp.util.SimpleAnimationUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class BasePopWindow {
    protected Activity mContext;
    protected PopupWindow popupWindow, popupWindowBk;
    protected View parent;
    protected View mView, mBackView, mPopView;
    protected boolean mIsShowBackground = false;
    private CommonCallback closeCallback;
    private Unbinder unbinder;
    private boolean isRegisterBus;
    protected boolean isForcus = true;
    protected String mBackgroundColor = "#60000000";
    protected int mAnimatorDurtion = 300;
    protected boolean isCloseAnimator = false;
    protected boolean mIsOutsideClose = true;
    protected boolean isDropDown = false;

    public BasePopWindow(Activity context) {
        initPopWindow(context);
        parent = UIUtils.getContentView(context);
    }

    private void initPopWindow(Activity context) {
        mContext = context;
        int resid = getLayoutId();
        //init popwindow.
        if(mIsShowBackground){
            mBackView = (LinearLayout) mContext.getLayoutInflater().inflate(R.layout.pop_background_view, null, false);
            popupWindowBk = new PopupWindow(mBackView, MATCH_PARENT, MATCH_PARENT, false);
            popupWindowBk.setClippingEnabled(false); //pop全屏设置
        }
        try {
            mView = mContext.getLayoutInflater().inflate(resid, null, false);
        }catch (Exception e){
            String msg = e.getMessage();
            e.printStackTrace();
        }

        if(isDropDown){
            popupWindow = new PopupWindow(mView, WRAP_CONTENT, WRAP_CONTENT, isForcus);
        }else {
            popupWindow = new PopupWindow(mView, MATCH_PARENT, WRAP_CONTENT, isForcus);
        }
        // 设置背景颜色变暗
        unbinder = ButterKnife.bind(this, mView);
        popupWindow.setClippingEnabled(false); //pop全屏设置
        popupWindow.setTouchable(true);//触摸可用
//        popupWindow.setOutsideTouchable(mIsOutsideClose); //点击控件外部消失
//        if(mIsOutsideClose) {
//            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
        popupWindow.setOnDismissListener(()->{
            //if(mIsOutsideClose) {
                close();
            //}
        });
        //mView.setFocusableInTouchMode(false); //按键设置
        mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if ((arg1 == KeyEvent.KEYCODE_HOME)
                        && (popupWindow != null && popupWindow.isShowing())) {
                    close();
                    return true;
                }
                return false;
            }
        });
    }

    public void close() {
        if(unbinder != null){
            unbinder.unbind();
            unbinder = null;
        }
        unregisterEvent();
        closePopWindow(popupWindow);
        closePopWindow(popupWindowBk);
        if(closeCallback != null){
            closeCallback.apply(null);
        }
    }

    private void closePopWindow(PopupWindow popupWindow){
        if(popupWindow != null && popupWindow.isShowing()) {
            if(mContext != null){
                popupWindow.dismiss();
                popupWindow = null;
            }

        }
    }

    public void setCloseCallback(CommonCallback callback){
        closeCallback = callback;
    }

    public boolean isShow(){
        if(popupWindow == null){
            return false;
        }else {
            return popupWindow.isShowing();
        }
    }

    public void registerEvent(){
        if(!isRegisterBus) {
            isRegisterBus = true;
            BusManager.register(this);
        }
    }

    public void unregisterEvent(){
        if(isRegisterBus){
            isRegisterBus = false;
            BusManager.unregister(this);
        }
    }

    public void postEvent(EventMessenger event) {
        BusManager.postEvent(event);
    }

    public void showPopWindow(View view, int xoff, int yoff, int gravity){
        if(isShow()){
            return;
        }
        //初始化界面
        initView();
        try {
            int[] xy = new int[2];
            view.getLocationInWindow(xy);
            popupWindow.showAtLocation(view, gravity,
                    xy[0] + xoff, xy[1] + yoff);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPopWindow(int gravity){
        if(isShow()){
            return;
        }
        //初始化界面
        initView();
        try {
            if(gravity == Gravity.BOTTOM){
                popupWindow.setAnimationStyle(R.style.upDownAnimStyle);
            }else if(gravity == Gravity.LEFT){
                popupWindow.setAnimationStyle(R.style.leftDownAnimStyle);
            }

            if(mIsShowBackground) {
                //显示背景
                //popupWindowBk.setAnimationStyle(R.style.upAlphaAnimStyle); //试了半天无效
                popupWindowBk.showAtLocation(mBackView, Gravity.CENTER, 0, 0);
                ObjectAnimator a = ObjectAnimator.ofInt(mBackView, "backgroundColor", Color.parseColor("#00000000"), Color.parseColor(mBackgroundColor));
                a.setEvaluator(new ArgbEvaluator());
                a.setDuration(mAnimatorDurtion);
                a.start();
            }
            popupWindow.showAtLocation(mView, gravity, 0,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initView(){
    }

    public void showFromBottom(){
        this.showPopWindow(Gravity.BOTTOM);
    }

    public void showFromCenter(){
        this.showPopWindow(Gravity.CENTER);
    }

    public int getLayoutId(){
        return 0;
    }

    public Activity getContext(){
        return mContext;
    }

    public void setBackgroundAlpha(float alpha){
        WindowManager.LayoutParams layoutParams = mContext.getWindow().getAttributes();
        layoutParams.alpha = alpha;
        mContext.getWindow().setAttributes(layoutParams);
    }

}
