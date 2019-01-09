package com.findyou.findyoueverywhere.utils.sdk.weixin;

import android.app.Activity;
import android.graphics.Bitmap;

import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.CommonCallback2;
import com.findyou.findyoueverywhere.constant.SdkConst;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.BitmapUtils;
import com.findyou.findyoueverywhere.utils.KeyboardUtils;
import com.findyou.findyoueverywhere.utils.json.JObject;
import com.findyou.findyoueverywhere.utils.sdk.callback.LoginCallback;
import com.findyou.findyoueverywhere.utils.sdk.callback.ShareCallback;
import com.findyou.findyoueverywhere.utils.share.ShareChannel;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/19 0019.
 * 集成微信登录，分享等功能
 */
public class WeiXinSDK implements IWXAPIEventHandler {
    private static final String TAG = "WeiXinSDK";

    //    private final String appID = Config.PIGTV_WEIXIN_APPID;
//    private final String appSecret = Config.PIG_WEIXIN_APPSECRET;
//    private Handler handler = new Handler(Looper.getMainLooper());
    private LoginCallback loginCallback;
    private CommonCallback2 shareCallback;
    //private IWeiXinAuthCallback authCallback; //微信授权回调
    private IWXAPI api = null;

    private WeiXinSDK() {
        createWXAPI();
    }

    private static class Singleton {
        private static final WeiXinSDK INTANCE = new WeiXinSDK();
    }

    public static WeiXinSDK getInstance() {
        return Singleton.INTANCE;
    }

    //微信api
    public IWXAPI createWXAPI() {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(app.getContext(), SdkConst.WEIXIN_APP_ID, false);
            api.registerApp(SdkConst.WEIXIN_APP_ID);
        }
        return api;
    }

    //是否安装了微信
    public boolean isInstalled() {
        api = createWXAPI();
        return api.isWXAppInstalled();
    }

    public interface AccessTokenCallback {
        void onResponse(String code);
    }

    //微信登录请求
//    public void login(LoginCallback callback){
//        try{
//            if(api == null){
//                api = getIWXAPI();
//            }
//
//            if (!api.isWXAppInstalled()) {
//                callback.onLoginError("请先安装微信客户端！");
//                return;
//            }
//
//            loginCallback = callback;
//
//            SendAuth.Req req = new SendAuth.Req();
//            req.scope = "snsapi_userinfo";
////            req.state = "com.douquhd.xiaotu"; //可以为package name
//            req.state = PigTvApp.getContext().getPackageName(); //可以为package name
//            NSLog.w("WeiXinSDK", "WeiXinSDK{} ... login() --> req.state = " + req.state);
//            api.sendReq(req);
//        }catch (Exception e){
//            callback.onLoginError(e.getMessage());
//        }
//    }
//
//    public interface IWeiXinAuthCallback{
//        void onWeixinAuthSucceed(String code);
//        void onWeixinAuthFailed();
//    }
//
//    public void setWeiXinAuthCallback(IWeiXinAuthCallback weiXinAuthCallback){
//        authCallback = weiXinAuthCallback;
//    }

//    //回应登录请求
//    private void responseLogin(String code){
//
//        if (null != code) {
//            //验证成功!
//            app.log("登录中...");
//            NSLog.w(TAG, "WeiXinSDK{} ... responseLogin() --> loginCallback = " + loginCallback);
//            if(loginCallback == null){
//                //微信登录验证
//                if(authCallback != null){
//                    authCallback.onWeixinAuthSucceed(code);
//                }
//                NSLog.w(TAG, "WeiXinSDK{} ... responseLogin() --> authCallback = " + authCallback);
//                return;
//            }
//
//            loginCallback.showTip("验证成功，登录中...");
//            // 获取微信access_token
//            getAccessToken(code, new AccessTokenCallback() {
//                @Override
//                public void onResponse(String response) {
//                    ResponseInfo responseInfo = new ResponseInfo(response);
//                    String access_token = responseInfo.getString("access_token");
//                    String openid = responseInfo.getString("openid");
//                    //通知APP微信验证成功,登录自己系统
////                    User user = new User();
//                    User.thirdLogin(access_token, openid, loginCallback);
//                }
//            });
//        } else {
//            if(authCallback != null){
//                authCallback.onWeixinAuthFailed();
//            }else if(loginCallback != null){
//                loginCallback.onLoginError("微信授权失败!");
//            }
//        }
//    }

    // 获取微信access_token
//    private void getAccessToken(String code, final AccessTokenCallback callback){
//        GetBuilder builder = new GetBuilder();
//        builder.url(Api.WEIXIN_ACCESS_TOKEN)
//                .addParams("appid", Config.PIGTV_WEIXIN_APPID)
//                .addParams("secret", Config.PIG_WEIXIN_APPSECRET)
//                .addParams("code", code)
//                .addParams("grant_type", "authorization_code")
//                .build().execute(new StringCallback() {
//            @Override
//            public void onError(Call request, Exception e, int id) {
//                try {
//                    loginCallback.onLoginError(e.getMessage());
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//
//                callback.onResponse(response);
//            }
//        });
//    }

    public void share(ShareChannel channel, JObject json, Bitmap bitmap, CommonCallback2 callback2) {
        if (channel == ShareChannel.SHARE_CHANNEL_WECHAT_MOMENT) {
            json.put("flag", SendMessageToWX.Req.WXSceneTimeline);
            //朋友圈
            WeiXinSDK.getInstance().share(json, bitmap, callback2);
        } else if (channel == ShareChannel.SHARE_CHANNEL_WECHAT_WECHAT) {
            json.put("flag", SendMessageToWX.Req.WXSceneSession);
            WeiXinSDK.getInstance().share(json, bitmap, callback2);
        }
    }

    //微信分享 flag: 0 微信好友, 1: 微信朋友圈
    private void share(JObject json, Bitmap bitmap, CommonCallback2 callback) {
        try {
            if (!isInstalled()) {
                callback.error("请先安装微信客户端!");
                return;
            }

            shareCallback = callback;

            int flag = json.getInt("flag");
            int shareType = json.getInt("type");
            String title = json.getString("title");

            WXMediaMessage msg = new WXMediaMessage();
            msg.title = title;
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            if (shareType == QQShare.SHARE_TO_QQ_TYPE_DEFAULT) { // == 1
                //分享网址
                String content = json.getString("content");
                String webUrl = json.getString("url");
                WXWebpageObject webpage = new WXWebpageObject(webUrl);
                msg.mediaObject = webpage;
                msg.description = content;
                req.transaction = buildTransaction("webpage");
                msg.setThumbImage(bitmap); // 缩略图 注意：限制内容大小不超过32KB
            } else if (shareType == QQShare.SHARE_TO_QQ_TYPE_IMAGE) { // == 5
                //分享图片
                WXImageObject imgObj = new WXImageObject(bitmap);
                msg.mediaObject = imgObj;
                msg.title = "";
                msg.description = "";
                req.transaction = buildTransaction("img"); // 图片 注意：内容大小不得超过 10MB
            }
            req.message = msg;
            req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
            api.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp resp) {
        //登录
        if (resp instanceof SendAuth.Resp) {
//            SendAuth.Resp res = (SendAuth.Resp) resp;
//            responseLogin(res.code);
        } else {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //分享成功
                    shareCallback.successful(null);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    //隐藏软键盘
                    Activity activity = ActivityManagerUtils.getInstance().getCurrentActivity();
                    boolean isKeyShow = KeyboardUtils.isKeyboardShow(activity);
                    KeyboardUtils.hideSoftInput(activity);
                    //分享取消
                    if (shareCallback != null) {
                        shareCallback.cancel("取消分享!");
                    }
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    //拒绝分享
                    shareCallback.error("拒绝分享!");
                    break;
            }
        }
    }
}
