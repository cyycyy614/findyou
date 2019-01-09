package com.findyou.findyoueverywhere.utils.sdk.callback;

import android.text.TextUtils;


/**
 * Created by Administrator on 2016/8/23 0023.
 */
public abstract class ShareCallback {

    public abstract void onShareSuccess();

    public void onShareError(String message) {
        if (TextUtils.isEmpty(message)) message = "对不起，分享失败";
        //ToastUtil.showShortToastSafe(message);
    }
//    void showTip(String message);
}
