package com.findyou.findyoueverywhere.utils;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;

import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseActivity;
import com.findyou.findyoueverywhere.controls.dialog.LoadingDialog;

public class LoadingUtils {

    static LoadingDialog popWindow;
    static BaseActivity activity;

    public static void show(BaseActivity activity, String text){
        LoadingUtils.activity = activity;
        if(activity.isDestroy()){
            return;
        }
        close();
        //Activity activity = ActivityManagerUtils.getInstance().getCurrentActivity();
        popWindow = new LoadingDialog(activity);
        if(!TextUtils.isEmpty(text)){
            popWindow.setText(text);
        }else{
            popWindow.hideText();
        }
        popWindow.showFromCenter();
    }

    private static Handler mHandler = new Handler(app.getContext().getMainLooper());

    public static void close(){
        if(activity == null){
            return;
        }
        if(activity.isDestroy()){
            return;
        }
        if(popWindow == null){
            return;
        }
        if(!popWindow.isShow()){
            return;
        }
        mHandler.post(()->{
            if(popWindow != null){
                popWindow.close();
                popWindow = null;
            }
        });

    }
}
