package com.findyou.findyoueverywhere.utils.sdk.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.CommonCallback2;
import com.findyou.findyoueverywhere.constant.SdkConst;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.json.JObject;
import com.findyou.findyoueverywhere.utils.sdk.callback.LoginCallback;
import com.findyou.findyoueverywhere.utils.sdk.callback.ShareCallback;
import com.findyou.findyoueverywhere.utils.share.ShareChannel;
import com.google.gson.JsonObject;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzonePublish;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class QQSDK {
    Tencent mTencent;
    private IUiListener loginUiListener;

    private LoginCallback loginCallback;
    private CommonCallback2 shareCallback;

    private QQSDK() {
        mTencent = Tencent.createInstance(SdkConst.QQ_APP_ID, app.getContext());
    }

    private static class Singleton {
        private static final QQSDK INTANCE = new QQSDK();
    }

    public static QQSDK getInstance() {
        return Singleton.INTANCE;
    }

    public void login(Activity activity, LoginCallback callback) {
        loginCallback = callback;
        //mTencent.login(activity, "get_user_info,add_t", this);
    }
//    public void callback(int requestCode, int resultCode, Intent data) {
//        mTencent.onActivityResultData(requestCode, resultCode, data, this);
//    }

    public void logout()
    {
        Context context = app.getContext();
        mTencent.logout(context);
    }

//    public void setLoginCallback(LoginCallback loginCallback) {
//        this.loginCallback = loginCallback;
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            //登录
            if (resultCode == Constants.ACTIVITY_OK) {
            }
        }else if(requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE){
            //分享
            if (resultCode == Constants.ACTIVITY_OK) {
                Tencent.onActivityResultData(requestCode, resultCode, data, shareUiListener);
            }
        }
    }
    private IUiListener shareUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            if(shareCallback != null){
                shareCallback.successful(o);
            }
        }

        @Override
        public void onError(UiError uiError) {
            if(shareCallback != null){
                shareCallback.error(uiError);
            }
        }

        @Override
        public void onCancel() {
            if(shareCallback != null){
                shareCallback.cancel(null);
            }
        }
    };

    public void share(Activity activity, ShareChannel channel, JObject json, CommonCallback2 callback2){
        if(channel == ShareChannel.SHARE_CHANNEL_QQ){
            shareToQQ(activity, json, callback2);
        }else if(channel == ShareChannel.SHARE_CHANNEL_QZONE) {
            shareToQzone(activity, json, callback2);
        }
    }

    // 分享到qq
    // params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, Environment.getExternalStorageDirectory().getAbsolutePath().concat("/a.png"));
    private void shareToQQ(Activity activity, JObject json, CommonCallback2 callback) {
        if(mTencent == null){
            return;
        }

        shareCallback = callback;

        int shareType = json.getInt("type");
        String image = json.getString("image");

        Bundle bundle = new Bundle();
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, json.getString("title"));//必填
        String appName = activity.getString(R.string.app_name);
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);

        if(shareType == QQShare.SHARE_TO_QQ_TYPE_DEFAULT){ // == 1
            //分享链接
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, json.getString("content"));
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, json.getString("url"));
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, image);
        }else if(shareType == QQShare.SHARE_TO_QQ_TYPE_IMAGE){ // == 5
            //分享图片
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, image);
        }
        mTencent.shareToQQ(activity, bundle, shareUiListener);
    }

    // 分享到qq空间
    private void shareToQzone(Activity activity, JObject json, CommonCallback2 callback){
        if(mTencent == null){
            return;
        }

        shareCallback = callback;

        int shareType = json.getInt("type");
        String title = json.getString("title");
        //String content = json.getString("content");
        String imagelist = json.getString("image");
        ArrayList<String> images = new ArrayList<>();
        if(!TextUtils.isEmpty(imagelist)){
            String imgstr[] = imagelist.split(";");
            for(int i=0; i<imgstr.length; i++){
                String item = imgstr[i];
                if(item.equals("")){
                    continue;
                }
                images.add(item);
            }
        }

        Bundle bundle = new Bundle();
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, title); //必填
        String appName = activity.getString(R.string.app_name);
        bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, appName);

        if(shareType == QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT){
            bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, json.getString("content"));
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, json.getString("url"));
            bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
            mTencent.shareToQzone(activity, bundle, shareUiListener);
        }else if(shareType == QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE){
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
            bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
            //发布
            mTencent.publishToQzone(activity, bundle, shareUiListener);
        }
    }

//    @Override
//    public void onComplete(Object o) {
//        JSONObject object = (JSONObject) o;
//        if(loginCallback != null){
//            String access_token = object.optString("access_token");
//            String openid = object.optString("openid");
////            String expiration = String.valueOf(object.optInt("expires_in"));
//            loginCallback.showTip("验证成功，登录中...");
////            User user = new User();
//            User.thirdLogin(access_token, openid, loginCallback);
//        }else if(shareCallback != null){
//            shareCallback.onShareSuccess();
//        }
//    }

//    @Override
//    public void onError(UiError uiError) {
//        if(loginCallback != null){
//            //NSLog.w("QQSDK", "QQSDK{} ... onError() --> uiError = " + uiError);
//            loginCallback.onLoginError(uiError.errorMessage);
//        }else if(shareCallback != null){
//            //shareCallback.onShareError(uiError.errorMessage);
//        }
//    }

//    @Override
//    public void onCancel() {
//        if(loginCallback != null) {
//            loginCallback.onLoginError("取消登录");
//        }else if(shareCallback != null){
//            shareCallback.onShareError("取消分享");
//        }
//    }

//    public IUiListener getListener() {
//        return this;
//    }
}
