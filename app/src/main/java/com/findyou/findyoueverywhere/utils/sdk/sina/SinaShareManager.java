//package com.findyou.findyoueverywhere.utils.sdk.sina;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//
//import com.findyou.findyoueverywhere.base.CommonCallback2;
//import com.findyou.findyoueverywhere.constant.SdkConst;
//import com.findyou.findyoueverywhere.utils.sdk.callback.ShareCallback;
//import com.sina.weibo.sdk.api.ImageObject;
//import com.sina.weibo.sdk.api.TextObject;
//import com.sina.weibo.sdk.api.WebpageObject;
////import com.sina.weibo.sdk.api.WeiboMessage;
////import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
////import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
////import com.sina.weibo.sdk.api.WeiboShareSDK;
//import com.sina.weibo.sdk.auth.AuthInfo;
//import com.sina.weibo.sdk.auth.Oauth2AccessToken;
//import com.sina.weibo.sdk.auth.WbAuthListener;
//import com.sina.weibo.sdk.exception.WeiboException;
//import com.sina.weibo.sdk.utils.Utility;
//
///**
// * Created by Administrator on 2016/8/19 0019.
// */
//public class SinaShareManager /*implements IWeiboHandler.Response*/ {
//    private static final String TAG = "SinaShareManager";
//
//    /**
//     * 文字
//     */
//    public static final int SINA_SHARE_WAY_TEXT = 1;
//    /**
//     * 图片
//     */
//    public static final int SINA_SHARE_WAY_PIC = 2;
//    /**
//     * 链接
//     */
//    public static final int SINA_SHARE_WAY_WEBPAGE = 3;
//
//    private static String sinaAppKey;
//    public static final String SCOPE =
//            "email,direct_messages_read,direct_messages_write,"
//                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
//                    + "follow_app_official_microblog," + "invitation_write";
//    /** 微博分享的接口实例 */
//    private IWeiboShareAPI sinaAPI;
//
//    //    private BaseActivity mActivity = null;
//    private Activity mActivity = null;
//
//    public IWeiboShareAPI getWeiboShareAPI(){
//        return sinaAPI;
//    }
//
//    private ShareCallback mShareCallback;
//    public ShareCallback getShareCallback(){
//        return mShareCallback;
//    }
////    public void register(BaseActivity context){
//    public void register(Activity context){
//        //获取appkey
//        if(sinaAppKey == null){
//            sinaAppKey = SdkConst.SINA_WB_APP_KEY;
//        }
//        //初始化微博分享代码
//        if(/*sinaAppKey != null &&*/ mActivity == null){
//            initSinaShare(context);
//        }
//    }
//
//    private static class Singleton {
//        private static final SinaShareManager INTANCE = new SinaShareManager();
//    }
//
//    public static SinaShareManager getInstance() {
//
//        return Singleton.INTANCE;
//    }
//
////    /**
////     * 当从微博返回到该Activity时，此接口函数会被调用
////     */
////    @Override
////    public void onResponse(BaseResponse baseResp) { // ms 无用 --> com/douquhd/xiaotu/umeng/wbapi/WBShareActivity.java  onResponse()
////        switch (baseResp.errCode) {
////            case com.sina.weibo.sdk.constant.WBConstants.ErrorCode.ERR_OK:
////                //Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
////
////                // 友盟 自定义事件功能  分享成功
////                MobclickAgent.onEvent(PigTvApp.getContext(), "ShareOK");
////
//////                mActivity.showToast("分享成功!");
////                ToastUtil.showShortToastSafe(mActivity, "分享成功!");
////
////                break;
////            case com.sina.weibo.sdk.constant.WBConstants.ErrorCode.ERR_FAIL:
////                //Toast.makeText(this, baseResp.errMsg + "分享失败！", Toast.LENGTH_LONG)
////                //        .show();
////
//////                String message = baseResp.errMsg;
//////                mActivity.showToast("分享失败!");
////                ToastUtil.showShortToastSafe(mActivity, "分享失败!");
////
////                break;
////            case com.sina.weibo.sdk.constant.WBConstants.ErrorCode.ERR_CANCEL:
////                //Toast.makeText(this, "取消分享", Toast.LENGTH_LONG).show();
//////                mActivity.showToast("取消分享!");
////                ToastUtil.showShortToastSafe(mActivity, "取消分享!");
////
////                break;
////        }
////    }
//
////    /**
////     * 新浪微博分享方法
////     * @param shareContent 分享的内容
////     */
////    public void share(final ShareContent shareContent, final BaseActivity context){
////
////        Oauth2AccessToken mAccessToken = ConfigSpHelper.getInstance().getSinaWbAccessToken();
////        if(mAccessToken.isSessionValid()){
////            //是有效的,不用登录验证
////        }else {
////            //要进行登录验证
////        }
////
////        //进行验证
////        SinaAuthLogin.getInstance(context).login(context, new LoginCallback() {
////
////            @Override
////            public void onLoginError(String message){
////                String str = message;
////            }
////
////            @Override
////            public void showTip(String message){
////                String str = message;
////            }
////
////            @Override
////            public void onLoginSuccess(UserInfo info) {
////
////                String sina_user_id = app.local.getItem("sina_open_id");
////                String sina_access_token = app.local.getItem("sina_access_token");
////                //进行分享
////
////                register(context);
////
////                mActivity = context;
////
////                if (sinaAPI == null) return;
////
////                switch (shareContent.getShareWay()) {
////                    case SINA_SHARE_WAY_TEXT:
////                        shareText(shareContent, context);
////                        break;
////                    case SINA_SHARE_WAY_PIC:
////                        sharePicture(shareContent, context);
////                        break;
////                    case SINA_SHARE_WAY_WEBPAGE:
////                        shareWebPage(shareContent, context);
////                        break;
////                }
////            }
////        });
////    }
//
//    /**
//     * 新浪微博分享方法
//     * @param shareContent 分享的内容
//     */
////    public void share(ShareContent shareContent, Activity context){
//    public void share(ShareContent shareContent, Activity context, CommonCallback2 callback2){
//
//        //mShareCallback = shareCallback;
//        Oauth2AccessToken mAccessToken = ConfigSpHelper.getInstance().getSinaWbAccessToken();
//        if(mAccessToken.isSessionValid()){
//            //是有效的,不用登录验证
//            //app.log("是有效的,不用登录验证");
//        }else {
//            //要进行登录验证
//            app.log("要进行登录验证");
//        }
//        SinaAuthLogin.getInstance(context).verified(context, new VerifiedCallback() {
//                    @Override
//                    public void onVerifiedError(String message) {
//                        //app.log("onVerifiedError:" + message);
//                        //分享失败
////                        BusManager.getIntance().post(new EventMessenger(EventMessenger.MSG_LIVE_SHARE_FAILED));
////                        mShareCallback.onShareError("对不起，微博认证失败");
//                        mShareCallback.onShareError(message);
//                    }
//
//                    @Override
//                    public void onVerifiedSuccess() {
//                        //app.log("onVerifiedSuccess");
//                        String sina_user_id = app.local.getItem("sina_open_id");
//                        String sina_access_token = app.local.getItem("sina_access_token");
//                        //进行分享
//
//                        register(context);
//
//                        mActivity = context;
//
//                        if (sinaAPI == null) return;
//
//                        switch (shareContent.getShareWay()) {
//                            case SINA_SHARE_WAY_TEXT:
//                                shareText(shareContent, context);
//                                break;
//                            case SINA_SHARE_WAY_PIC:
//                                sharePicture(shareContent, context);
//                                break;
//                            case SINA_SHARE_WAY_WEBPAGE:
//                                shareWebPage(shareContent, context);
//                                break;
//                        }
//                    }
//                });
//
////        if(true){
////            return;
////        }
////        //进行验证
////        SinaAuthLogin.getInstance(context).login(context, new LoginCallback() {
////
////            @Override
////            public void onLoginError(String message){
////                app.log("wei bo share error:" + message);
////                ToastUtil.showShortToastSafe(message);
////            }
////
////            @Override
////            public void showTip(String message){
////                app.log("wei bo share tips:" + message);
////                ToastUtil.showShortToastSafe(message);
////            }
////
////            @Override
////            public void onLoginSuccess(UserInfo info) {
////                app.log("wei bo share success.");
////
////                String sina_user_id = app.local.getItem("sina_open_id");
////                String sina_access_token = app.local.getItem("sina_access_token");
////                //进行分享
////
////                register(context);
////
////                mActivity = context;
////
////                if (sinaAPI == null) return;
////
////                switch (shareContent.getShareWay()) {
////                    case SINA_SHARE_WAY_TEXT:
////                        shareText(shareContent, context);
////                        break;
////                    case SINA_SHARE_WAY_PIC:
////                        sharePicture(shareContent, context);
////                        break;
////                    case SINA_SHARE_WAY_WEBPAGE:
////                        shareWebPage(shareContent, context);
////                        break;
////                }
////            }
////        });
//    }
//
//    //验证分享
//    public void share1(final ShareContent shareContent, final BaseActivity context){
//
//        String sina_user_id = app.local.getItem("sina_open_id");
//        String sina_access_token = app.local.getItem("sina_access_token");
//        //进行分享
//
//        register(context);
//
//        mActivity = context;
//
//        if (sinaAPI == null) return;
//        switch (shareContent.getShareWay()) {
//            case SINA_SHARE_WAY_TEXT:
//                shareText(shareContent, context);
//                break;
//            case SINA_SHARE_WAY_PIC:
//                sharePicture(shareContent, context);
//                break;
//            case SINA_SHARE_WAY_WEBPAGE:
//                shareWebPage(shareContent, context);
//                break;
//        }
//    }
//
//    public static final String REDIRECT_URL = "http://www.sina.com";
//
//    public void sendSeq(SendMessageToWeiboRequest request){
//        AuthInfo authInfo = new AuthInfo(mActivity, Config.PIG_SINA_APPKEY, REDIRECT_URL, SCOPE);
//        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(PigTvApp.getContext());
//        String token = "";
//        if (accessToken != null) {
//            token = accessToken.getToken();
//        }
//
//        sinaAPI.sendRequest(mActivity, request, authInfo, token, new WeiboAuthListener() {
//
//            @Override
//            public void onWeiboException( WeiboException arg0 ) {
//                NSLog.e(TAG, "====== SinaShareManager{} ... onWeiboException() ======");
//            }
//
//            @Override
//            public void onComplete( Bundle bundle ) {
//                // TODO Auto-generated method stub
//                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
//                AccessTokenKeeper.writeAccessToken(PigTvApp.getContext(), newToken);
//            }
//
//            @Override
//            public void onCancel() {
//                NSLog.e(TAG, "====== SinaShareManager{} ... onCancel() ======");
//            }
//        });
//    }
//
//    /*
//     * 分享文字
//     */
//    private void shareText(ShareContent shareContent, Activity context){
//        //初始化微博的分享消息
//        WeiboMessage weiboMessage = new WeiboMessage();
//        weiboMessage.mediaObject = getTextObj(shareContent.getContent());
//        //初始化从第三方到微博的消息请求
//        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
//        request.transaction = buildTransaction("sinatext");
//        request.message = weiboMessage;
//        //发送请求信息到微博，唤起微博分享界面
//        sendSeq(request);
//        /*
//        String loginEntry = app.local.getItem("LoginEntry");
//        if(loginEntry.equals("sina")){
//            sinaAPI.sendRequest(context, request);
//        }else {
//            sendSeq(request);
//        }
//        */
//    }
//
//    /*
//     * 分享图片
//     */
//    private void sharePicture(ShareContent shareContent, Activity context){
//
//        WeiboMessage weiboMessage = new WeiboMessage();
//        weiboMessage.mediaObject = getImageObj(shareContent.getBitmap(), context);
//        //初始化从第三方到微博的消息请求
//        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
//        request.transaction = buildTransaction("sinatext");
//        request.message = weiboMessage;
//        //发送请求信息到微博，唤起微博分享界面
//        sendSeq(request);
//
//        /*
//        String loginEntry = app.local.getItem("LoginEntry");
//        if(loginEntry.equals("sina")){
//            sinaAPI.sendRequest(context, request);
//        }else {
//            sendSeq(request);
//        }
//        */
//    }
//
//    private void shareWebPage(ShareContent shareContent, Activity context){
//        WeiboMessage weiboMessage = new WeiboMessage();
//        weiboMessage.mediaObject = getWebpageObj(shareContent, context);
//        //初始化从第三方到微博的消息请求
//        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
//        request.transaction = buildTransaction("sinatext");
//        request.message = weiboMessage;
//        //发送请求信息到微博，唤起微博分享界面
//        //
//        sendSeq(request);
//        /*
//        String loginEntry = app.local.getItem("LoginEntry");
//        if(loginEntry.equals("sina")){
//            sinaAPI.sendRequest(context, request);
//        }else {
//            sendSeq(request);
//        }
//        */
//    }
//
//    /**
//     * @author lixiaoqiang
//     *
//     */
//    private abstract class ShareContent{
//        protected abstract int getShareWay();
//        protected abstract String getContent();
//        protected abstract String getTitle();
//        protected abstract String getURL();
//        protected abstract int getPicResource();
//        protected abstract Bitmap getBitmap();
//    }
//
//    /**
//     * 设置分享文字的内容
//     * @author Administrator
//     *
//     */
//    public class ShareContentText extends ShareContent{
//        private String content;
//
//        /**
//         * 构造分享文字类分享的文字内容
//         * @param content
//         */
//        public ShareContentText(String content){
//            this.content = content;
//        }
//
//        @Override
//        protected String getContent() {
//
//            return content;
//        }
//
//        @Override
//        protected String getTitle() {
//            return null;
//        }
//
//        @Override
//        protected String getURL() {
//            return null;
//        }
//
//        @Override
//        protected int getPicResource() {
//            return -1;
//        }
//
//        @Override
//        protected int getShareWay() {
//            return SINA_SHARE_WAY_TEXT;
//        }
//
//        @Override
//        protected Bitmap getBitmap(){
//            return null;
//        }
//
//    }
//
//    /**
//     * 设置分享图片的内容
//     * @author Administrator
//     *
//     */
//    public class ShareContentPic extends ShareContent{
//        private int picResource;
//        private Bitmap bitmap;
//
//        public ShareContentPic(int picResource){
//            this.picResource = picResource;
//        }
//
//        public ShareContentPic(Bitmap bitmap){
//            this.bitmap = bitmap;
//        }
//
//        @Override
//        protected String getContent() {
//            return null;
//        }
//
//        @Override
//        protected String getTitle() {
//            return null;
//        }
//
//        @Override
//        protected String getURL() {
//            return null;
//        }
//
//        @Override
//        protected int getPicResource() {
//            return picResource;
//        }
//
//        @Override
//        protected int getShareWay() {
//            return SINA_SHARE_WAY_PIC;
//        }
//
//        @Override
//        protected Bitmap getBitmap(){
//            return bitmap;
//        }
//    }
//
//    /**
//     * 设置分享链接的内容
//     * @author Administrator
//     *
//     */
//    public class ShareContentWebpage extends ShareContent{
//        private String title;
//        private String content;
//        private String url;
//        private int picResource;
//        public Bitmap bitmap;
//
//        public ShareContentWebpage(String title, String content,
//                                   String url, int picResource){
//            this.title = title;
//            this.content = content;
//            this.url = url;
//            this.picResource = picResource;
//        }
//
//        public ShareContentWebpage(String title, String content, String url, Bitmap bitmap){
//            this.title = title;
//            this.content = content;
//            this.url = url;
//            this.bitmap = bitmap;
//        }
//
//        @Override
//        protected String getContent() {
//            return content;
//        }
//
//        @Override
//        protected String getTitle() {
//            return title;
//        }
//
//        @Override
//        protected String getURL() {
//            return url;
//        }
//
//        @Override
//        protected int getPicResource() {
//            return picResource;
//        }
//
//        @Override
//        protected int getShareWay() {
//            return SINA_SHARE_WAY_WEBPAGE;
//        }
//
//        public Bitmap getBitmap(){
//            return bitmap;
//        }
//    }
//
//    /**
//     * 创建文本消息对象。
//     *
//     * @return 文本消息对象。
//     */
//    private TextObject getTextObj(String text) {
//        TextObject textObject = new TextObject();
//        textObject.text = text;
//        return textObject;
//    }
//
//    private ImageObject getImageObj(int picResource, Context context){
//        ImageObject imageObject = new ImageObject();
//        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), picResource);
//        imageObject.setImageObject(bmp);
//        return imageObject;
//    }
//
//    private ImageObject getImageObj(Bitmap bitmap, Context context){
//        ImageObject imageObject = new ImageObject();
//        imageObject.setImageObject(bitmap);
//        return imageObject;
//    }
//
//    private WebpageObject getWebpageObj(ShareContent shareContent, Context context){
//        WebpageObject mediaObject = new WebpageObject();
//        mediaObject.identify = Utility.generateGUID();
//        mediaObject.title = shareContent.getTitle();
//        mediaObject.description = shareContent.getContent();
//
//        // 设置 Bitmap 类型的图片到视频对象里
//        Bitmap bmp = shareContent.getBitmap();  //BitmapFactory.decodeResource(context.getResources(), shareContent.getPicResource());
//        mediaObject.setThumbImage(bmp);
//        mediaObject.actionUrl = shareContent.getURL();
//        mediaObject.defaultText = shareContent.getContent();
//
//        return mediaObject;
//    }
//
////    private void initSinaShare(BaseActivity context){
//    private void initSinaShare(Activity context){
//        // 创建微博 SDK 接口实例
//        sinaAPI = WeiboShareSDK.createWeiboAPI(context, sinaAppKey);
//        //检查版本支持情况
//        checkSinaVersin(context);
//        sinaAPI.registerApp();
//    }
//
////    private void checkSinaVersin(final BaseActivity context) {
//    private void checkSinaVersin(final Activity context) {
//        // 获取微博客户端相关信息，如是否安装、支持 SDK 的版本
//        boolean isInstalledWeibo = sinaAPI.isWeiboAppInstalled();
//        //int supportApiLevel = sinaAPI.getWeiboAppSupportAPI();
//
//        // 如果未安装微博客户端，设置下载微博对应的回调
//
//        if (!isInstalledWeibo) {
//            //提示下载新浪微博客户端
////            context.showToast("请先下载新浪微博客户端!");
////            context.showToast("请先下载新浪微博客户端!");
//            ToastUtil.showShortToastSafe(context, "请先下载新浪微博客户端!");
//
//            /*
//            sinaAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
//                @Override
//                public void onCancel() {
//                    Toast.makeText(context,
//                            "取消下载",
//                            Toast.LENGTH_SHORT).show();
//                }
//            });
//            */
//        }
//    }
//
//    private String buildTransaction(final String type) {
//        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
//    }
//}