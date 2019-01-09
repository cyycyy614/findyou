package com.findyou.findyoueverywhere.ui.login;

import android.app.Activity;
import android.text.TextUtils;

import com.findyou.findyoueverywhere.api.http.ApiUser;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.bean.common.UserInfoBean;
import com.findyou.findyoueverywhere.ui.main.MainActivity;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.LocalStorage;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.http.HttpResponse;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

public class LoginUtils {
    ///////////////////////////////////////////////////////////////////////////////////////////
    //QQ登录
    //所需两个对象
    private static Tencent mTencent; // qq主操作对象
    private static IUiListener loginListener;//qq事件对象
    // 初始化qq主操作对象 腾讯提供测试id 222222
//    public static IUiListener doQQLogin(Activity activity){
//        LoadingUtils.show("登录中...");
//        String qq_app_id = UIUtils.getString(R.string.qq_app_id).replace("tencent", "");
//        mTencent = Tencent.createInstance(qq_app_id, MyApp.getContext());
//        if(mTencent == null){
//            return null;
//        }
//        loginListener = new IUiListener() {
//            @Override
//            public void onError(UiError e) {
//                // TODO Auto-generated method stub
//                LoadingUtils.close();
//                ToastUtils.showToast(activity, e.errorMessage);
//            }
//            /**
//             * 返回json数据样例
//             *
//             * {"ret":0,"pay_token":"D3D678728DC580FBCDE15722B72E7365",
//             * "pf":"desktop_m_qq-10000144-android-2002-",
//             * "query_authority_cost":448,
//             * "authority_cost":-136792089,
//             * "openid":"015A22DED93BD15E0E6B0DDB3E59DE2D",
//             * "expires_in":7776000,
//             * "pfkey":"6068ea1c4a716d4141bca0ddb3df1bb9",
//             * "msg":"",
//             * "access_token":"A2455F491478233529D0106D2CE6EB45",
//             * "login_cost":499}
//             */
//            @Override
//            public void onComplete(Object value) {
//                // TODO Auto-generated method stub
//                LoadingUtils.close();
//                if (value == null) {
//                    return;
//                }
//                try {
//                    JSONObject jo = (JSONObject) value;
//                    int ret = jo.getInt("ret");
//                    System.out.println("json=" + String.valueOf(jo));
//                    if (ret == 0) {
//                        //登录成功
//                        String username = jo.getString("openid");
//                        String accessToken = jo.getString("access_token");
//                        String expires = jo.getString("expires_in");
//                        mTencent.setOpenId(username);
//                        mTencent.setAccessToken(accessToken, expires);
//                        String md5password = ToolUtils.MD5(UIUtils.getString(R.string.qq_login_password)).toLowerCase();
//
//                        log.debug("1");
//                        ApiUser.doThirdLogin(username, md5password, LoginType.QQ.ordinal(), (res)->{
//                            log.debug("2");
//                            loginSuccessful(res, username, md5password, LoginType.QQ.ordinal());
//                        });
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//            }
//            @Override
//            public void onCancel() {
//                // TODO Auto-generated method stub
//                LoadingUtils.close();
//                ToastUtils.showToast(activity, "登录取消,请重试!");
//            }
//        };
//        //如果session无效，就开始登录
//        String scope = "all";
//        //if (!mTencent.isSessionValid()) {
//            //开始qq授权登录
//            mTencent.login(activity, scope, loginListener);
//        //}
//
//        return loginListener;
//    }

    private static IWXAPI mApi;
//    public static void doWeiXinLogin(Activity activity){
//        String weixin_app_id = UIUtils.getString(R.string.weixin_app_id);
//        mApi = WXAPIFactory.createWXAPI(activity, weixin_app_id, false);
//        mApi.registerApp(weixin_app_id);
//        if (mApi != null && mApi.isWXAppInstalled()) {
//            SendAuth.Req req = new SendAuth.Req();
//            req.scope = "snsapi_userinfo";
//            req.state = AppUtils.getAppPackageName(); //
//            mApi.sendReq(req);
//        } else{
//            ToastUtils.showToast(MyApp.getContext(),"用户未安装微信");
//        }
//    }

    public static IWXAPI getWeiXinAPI(){
        return mApi;
    }

    public static void startMainActivity() {
        //get access token.
        String username = LocalStorage.getItem("username");
        String password = LocalStorage.getItem("password");
//        OAuth2Utils.getAuthToken(username, password, (res)->{
//            log.debug("6");
//            Activity activity = ActivityManagerUtils.getInstance().getCurrentActivity();
//            if(activity != null) {
//                log.debug("7");
//                ActivityManagerUtils.startActivity(activity, MainActivity.class);
//                log.debug("8");
//                activity.finish();
//                log.debug("9");
//            }
//        });
        Activity activity = ActivityManagerUtils.getInstance().getCurrentActivity();
        ActivityManagerUtils.startActivity(activity, MainActivity.class, null);
    }

    public static void startLoginActivity() {
        Activity activity = ActivityManagerUtils.getInstance().getCurrentActivity();
        if(activity != null) {
            ActivityManagerUtils.startActivity(activity, LoginFragment.class, null);
        }
    }

    public static void loginSuccessful(HttpResponse res, String username, String md5password, int loginType){
        UserInfoBean bean = null;
        if(res != null){
            if(res.data != null){
                bean = JsonUtils.convert(res.data, UserInfoBean.class);
                if(bean != null){
                    //ApiApp.registerChannelInfo(bean.uid, null); //注册渠道信息,用于统计
                }
            }
            ToastUtils.showToast(app.getContext(), res.message);
        }else{
            bean = new UserInfoBean();
            ToastUtils.showToast(app.getContext(), "登录成功!");
        }
        if(true){
            //return;
        }
        //存储凭证，以便下次登录时用
        LocalStorage.setItem("username", username);
        LocalStorage.setItem("password", md5password);
        LocalStorage.setItem("loginType", String.valueOf(loginType));
        LocalStorage.setItem("expires" ,"10000");
        app.me = bean;
        startMainActivity();
    }

    public static void defaultLogin(){
        //登录检查
        String username = LocalStorage.getItem("username");
        String password = LocalStorage.getItem("password");
        String loginType = LocalStorage.getItem("loginType");
        if(TextUtils.isEmpty(loginType)){
            loginType = LoginType.mobile.toString(); //默认
        }
        LocalStorage.setItem("loginType", loginType);
        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
            //登录请求
            ApiUser.login(username, password, loginType, (res)->{
                if(res == null){
                    return;
                }
                UserInfoBean bean = JsonUtils.convert(res.data, UserInfoBean.class);
                app.me = bean;
                //有登录过则进入主界面
                startMainActivity();
            }, (res)->{
                //没登录过则进入登录页
                if(res.code != 200){
                    if(res.code == 1003){
                        //没有此用户
                        LocalStorage.setItem("username", "");
                        LocalStorage.setItem("password", "");
                    }else {
                        ToastUtils.showToast(app.getContext(), res.message);
                    }
                }
                startLoginActivity();
            });
        }else {
            //没登录过则进入登录页
            startLoginActivity();
        }
    }
}
