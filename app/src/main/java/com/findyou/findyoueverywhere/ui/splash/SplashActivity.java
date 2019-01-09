package com.findyou.findyoueverywhere.ui.splash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseActivity;
import com.findyou.findyoueverywhere.base.CommonCallback2;
import com.findyou.findyoueverywhere.ui.login.GuideActivity;
import com.findyou.findyoueverywhere.ui.login.LoginUtils;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.LocalStorage;
import com.findyou.findyoueverywhere.utils.NotificationUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.sdk.sina.SinaWeiboSDK;
import com.findyou.findyoueverywhere.utils.sdk.sina.content.ShareContent;

import static com.findyou.findyoueverywhere.utils.sdk.sina.content.ShareContent.SINA_SHARE_WAY_WEBPAGE;

public class SplashActivity extends BaseActivity {
    public int getLayoutId(){
        return R.layout.activity_splash;
    }

    public void initView(){
        //LoginUtils.defaultLogin();
//        if(TextUtils.isEmpty(username) && TextUtils.isEmpty(password)){
//            ActivityManagerUtils.startActivity(this, LoginFragment.class, null);
//        }else {
//            //自动登录
//            ApiUser.login(username, password, LoginType.mobile.toString(), (res)->{
//                UserInfoBean bean = JsonUtils.convert(res.data, UserInfoBean.class);
//                app.me = bean;
//                ToastUtils.showToast(app.getContext(), "登录成功!");
//                LocalStorage.setItem("username", username);
//                LocalStorage.setItem("password", password);
//                ActivityManagerUtils.startActivity(this, MainActivity.class, null);
//            }, null);
//        }
    }
    Thread thread;
    @Override
    public void onResume(){
        super.onResume();
        autoLogin();
        if(true){
            return;
        }

        if(thread != null){
            thread.interrupt();
        }
        NotificationUtils.getInstance().createNotificationChannel();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000 * 10);
                        NotificationUtils.getInstance().sendNotification("hello,", "world");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void autoLogin(){
        String isfirst = LocalStorage.getItem("first_application");
        if(TextUtils.isEmpty(isfirst)){
            //第一次进入需引导
            ActivityManagerUtils.startActivity(this, GuideActivity.class, null);
        }else{
            LoginUtils.defaultLogin();
        }
        if(true){
            return;
        }
        SinaWeiboSDK.getInstance().register(this);
        Bitmap bitmap = BitmapFactory.decodeResource(app.getContext().getResources(), R.drawable.logo);
        ShareContent content = new ShareContent(SINA_SHARE_WAY_WEBPAGE, "title", "content", "http://www.baidu.com", bitmap);
        SinaWeiboSDK.getInstance().share(content, new CommonCallback2() {
            @Override
            public void successful(Object obj) {
                ToastUtils.showToast(app.getContext(), "分享成功!");
            }
            @Override
            public void error(Object obj) {
                ToastUtils.showToast(app.getContext(), "失败成功!");
            }
            @Override
            public void cancel(Object obj) {
                ToastUtils.showToast(app.getContext(), "取消成功!");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SinaWeiboSDK.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
