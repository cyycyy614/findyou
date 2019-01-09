package com.findyou.findyoueverywhere.utils.http;

import android.text.TextUtils;

import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.CommonCallback;
import com.findyou.findyoueverywhere.bean.common.OAuthTokenBean;
import com.findyou.findyoueverywhere.constant.ApiConst;
import com.findyou.findyoueverywhere.utils.Base64Utils;
import com.findyou.findyoueverywhere.utils.LocalStorage;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.findyou.findyoueverywhere.utils.log.log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OAuth2Utils {
    public static boolean isStop = false;
    public static void getAuthToken(String username, String password, CommonCallback callback) {
        try {
            if(!HttpClient.isUsed()){
                if(callback != null) {
                    callback.apply(null);
                }
                return;
            }
            String basic = username + ":" + password;
            basic = Base64Utils.encode(basic);
            basic = "Basic " + basic;
            String url = ApiConst.BASE_ROOT_URL + "token";
            OkHttpClient client = HttpClient.getHttpClient();
            RequestBody body =  new FormBody.Builder()
                    .add("grant_type","password")
                    .add("username",username)
                    .add("password",password)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", basic)
                    .post(body)
                    .build(); //Post请求
            //可以省略，默认是GET请求
            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    HttpClient.onExceptionHandler(url, e);
                }

                @Override
                public void onResponse(Call call, Response res) throws IOException {
                    try {
                        if (res.code() == 200) {
                            String jsonData = res.body().string();
                            OAuthTokenBean token = JsonUtils.convert(jsonData, OAuthTokenBean.class);
                            if(token != null){
                                app.oAuthToken = token;
                                if(callback != null){
                                    callback.apply(token);
                                }
                            }
                        } else {
                            //吐丝
                            ToastUtils.showToast(app.getContext(), "OAuth2请求异常,请检查网络!");
                            log.write("request url:" + url);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void refreshAuthToken(String username, String password, CommonCallback callback){
        try {
            if(app.oAuthToken == null){
                return;
            }
            if(TextUtils.isEmpty(app.oAuthToken.refresh_token)){
                return;
            }
            String basic = username + ":" + password;
            basic = Base64Utils.encode(basic);
            basic = "Basic " + basic;
            String url = ApiConst.BASE_ROOT_URL + "token";
            OkHttpClient client = HttpClient.getHttpClient();
            RequestBody body =  new FormBody.Builder()
                    .add("grant_type","refresh_token")
                    .add("refresh_token", app.oAuthToken.refresh_token)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", basic)
                    .post(body)
                    .build(); //Post请求
            //可以省略，默认是GET请求
            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    HttpClient.onExceptionHandler(url, e);
                }

                @Override
                public void onResponse(Call call, Response res) throws IOException {
                    try {
                        if (res.code() == 200) {
                            String jsonData = res.body().string();
                            OAuthTokenBean token = JsonUtils.convert(jsonData, OAuthTokenBean.class);
                            if(token != null){
                                //tcp refresh token.
                                //synchronized (TcpClient.getInstance().lock) {
//                                    log.debug("the old access token is:" + app.OAuthToken.refresh_token);
                                    //refreshSocketAccessToken(token.refresh_token);
                                    app.oAuthToken = token;
//                                    log.debug("the new access token is:" + app.OAuthToken.refresh_token);
                                //}
                                if(callback != null){
                                    callback.apply(token);
                                }
                            }
                        } else {
                            //吐丝
                            ToastUtils.showToast(app.getContext(), "请求异常,请检查网络!");
                            log.write("request url:" + url);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void refreshSocketAccessToken(String newToken){
//        if(app.Me == null){
//            return;
//        }
//        JObject jo = new JObject();
//        jo.put("cmd", NetworkProtocol.CMD_UPDATE_ACCESS_TOKEN);
//        jo.put("userid", app.Me.userid);
//        jo.put("new_token", newToken);
//        TcpClient.getInstance().doSend(jo);
//    }

    private static Thread mThread;
    public static void refreshTokenThread(){
        isStop = false;
        mThread = new Thread(()->{
            while (!isStop) {
                //log.debug("refreshTokenThread");
                try {
                    Thread.sleep(60 * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String username = LocalStorage.getItem("username");
                String password = LocalStorage.getItem("password");
                refreshAuthToken(username, password, null);
            }
        });
        mThread.start();
    }

    public static void stopRefreshToken(){
        if(mThread != null){
            mThread.interrupt();
            mThread = null;
        }
        isStop = true;
    }
}
