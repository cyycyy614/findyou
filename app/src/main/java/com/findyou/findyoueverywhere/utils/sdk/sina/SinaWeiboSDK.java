package com.findyou.findyoueverywhere.utils.sdk.sina;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.CommonCallback2;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.sdk.sina.content.ShareContent;
import com.findyou.findyoueverywhere.utils.sdk.weixin.WeiXinSDK;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

public class SinaWeiboSDK {
    public static final int SHARE_CLIENT = 1;
    public static final int SHARE_ALL_IN_ONE = 2;
    private WbShareHandler shareHandler;
    private int mShareType = SHARE_CLIENT;
    int flag = 0;
    private AuthInfo mAuthInfo;

    public SinaWeiboSDK(){

    }

    private static class Singleton {
        private static final SinaWeiboSDK INTANCE = new SinaWeiboSDK();
    }

    public static SinaWeiboSDK getInstance() {
        return SinaWeiboSDK.Singleton.INTANCE;
    }

    public void register(Activity activity){
        mAuthInfo = new AuthInfo(activity, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        WbSdk.install(activity, mAuthInfo);
        shareHandler = new WbShareHandler(activity);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xffff5500);
    }

    private WbShareCallback shareCallback = new WbShareCallback() {
        @Override
        public void onWbShareSuccess() {
            if(callback2 != null){
                callback2.successful(null);
            }
        }

        @Override
        public void onWbShareCancel() {
            if(callback2 != null){
                callback2.cancel(null);
            }
        }

        @Override
        public void onWbShareFail() {
            if(callback2 != null){
                callback2.error(null);
            }
        }
    };


    /**
     * 创建文本消息对象。
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = "1111111" ;//getSharedText();
        textObject.title = "xxxx";
        textObject.actionUrl = "http://www.baidu.com";
        return textObject;
    }

    /**
     * 创建图片消息对象。
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        Bitmap  bitmap = BitmapFactory.decodeResource(app.getContext().getResources(), R.drawable.logo);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(ShareContent content) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = "测试title";
        mediaObject.description = "测试描述";
        Bitmap bitmap = BitmapFactory.decodeResource(app.getContext().getResources(), R.drawable.logo);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = "http://news.sina.com.cn/c/2013-10-22/021928494669.shtml";
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        shareHandler.doResultIntent(data,shareCallback);
    }

    public CommonCallback2 callback2;
    public void share(ShareContent content, CommonCallback2 callback2){
        if(!shareHandler.isWbAppInstalled()){
            //ToastUtils.showToast(app.getContext(), "请先");
        }
        this.callback2 = callback2;
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.mediaObject = getWebpageObj(content);
        shareHandler.shareMessage(weiboMessage, false);
    }
}
