package com.findyou.findyoueverywhere.wxapi;

import android.content.Intent;

import com.findyou.findyoueverywhere.base.BaseActivity;
import com.findyou.findyoueverywhere.utils.KeyboardUtils;
import com.findyou.findyoueverywhere.utils.sdk.weixin.WeiXinSDK;

/**
 * 微信登录回调页面
 * Created by ntop on 15/9/4.
 */
public class WXEntryActivity extends BaseActivity {

    @Override
    public void initView(){
        WeiXinSDK weiXinSDK = WeiXinSDK.getInstance();
        boolean result = weiXinSDK.createWXAPI().handleIntent(getIntent(), weiXinSDK); //调起response回调
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        WeiXinSDK weiXinSDK = WeiXinSDK.getInstance();
        boolean result = weiXinSDK.createWXAPI().handleIntent(getIntent(), weiXinSDK); //调起response回调
        //隐藏键盘
        boolean isKeyShow = KeyboardUtils.isKeyboardShow(this);
        KeyboardUtils.hideSoftInput(this);
        finish();
    }
}

