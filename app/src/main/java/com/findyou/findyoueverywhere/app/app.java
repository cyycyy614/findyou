package com.findyou.findyoueverywhere.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.util.Utils;
import com.findyou.findyoueverywhere.bean.common.OAuthTokenBean;
import com.findyou.findyoueverywhere.bean.common.UserInfoBean;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class app extends Application {

    private static app mInstance = null;
    public static UserInfoBean me;
    public static String dbFileName = "findyou.db"; //数据库
    public static OAuthTokenBean oAuthToken;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //AppUtils组件
        Utils.init(this);
        registerActivity();
        //百度地图
        SDKInitializer.initialize(getApplicationContext());
        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //UI模块
        UIUtils.init();
    }

    public static Context getContext() {
        return mInstance;
    }

    public void registerActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                //app.log("start activity:" + activity.getClass().getName());
                ActivityManagerUtils.getInstance().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                ActivityManagerUtils.getInstance().setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
//                ActivityManagerUtil.getInstance().setPreviousActivity(activity);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityManagerUtils.getInstance().removeActivity(activity);
            }
        });
    }
}

