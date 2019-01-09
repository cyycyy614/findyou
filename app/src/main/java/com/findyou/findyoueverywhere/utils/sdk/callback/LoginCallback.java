package com.findyou.findyoueverywhere.utils.sdk.callback;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public interface LoginCallback{
    void onLoginSuccess(int userInfo);
    void onLoginError(String message);
    void showTip(String message);
}

