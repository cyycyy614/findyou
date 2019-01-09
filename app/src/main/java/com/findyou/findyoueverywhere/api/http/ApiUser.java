package com.findyou.findyoueverywhere.api.http;


import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.constant.ApiConst;
import com.findyou.findyoueverywhere.utils.http.HttpCallback;
import com.findyou.findyoueverywhere.utils.http.HttpClient;
import com.findyou.findyoueverywhere.utils.http.QueryString;

public class ApiUser {
    //////////////////////////////////////////////////////////////////////////////////////////
    //登录，注册
    public static void sendAuthCode(String phoneNumber, int type, HttpCallback callback){
        //参数
        QueryString q = new QueryString();
        q.add("username", phoneNumber);
        q.add("type", type);
        //Post请求
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_SEND_CODE), q, callback);
    }

    public static void login(String username, String password, String loginType, HttpCallback callback, HttpCallback callback1){
        //参数
        QueryString q = new QueryString();
        q.add("username", username);
        q.add("password", password);
        q.add("loginType", loginType);
        //Post请求
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_LOGIN), q, callback, callback1);
    }

    public static void changePassword(int userid, String password, HttpCallback callback){
        //参数
        QueryString q = new QueryString();
        q.add("uid", userid);
        q.add("password", password);
        //Post请求
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_CHANGE_PASSWORD), q, callback);
    }

    public static void register(String username, String password, HttpCallback callback){
        //参数
        QueryString q = new QueryString();
        q.add("username", username);
        q.add("password", password);
        //Post请求
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_REGISTER), q, callback);
    }

    public static void resetPassword(String username, String password, HttpCallback callback){
        //参数
        QueryString q = new QueryString();
        q.add("username", username);
        q.add("password", password);
        //Post请求
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_RESET_PASSWORD), q, callback);
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    //更改用户资料
    public static void updateNickname(String nickname, HttpCallback callback){
        int uid = app.me.uid;
        QueryString q = new QueryString();
        q.add("uid", uid);
        q.add("value", nickname);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_USER_UPDATE_NICKNAME), q, callback);
    }

    public static void updateSex(int sex, HttpCallback callback){
        int uid = app.me.uid;
        QueryString q = new QueryString();
        q.add("uid", uid);
        q.add("value", sex);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_USER_UPDATE_SEX), q, callback);
    }

    public static void updateSign(String sign, HttpCallback callback){
        int uid = app.me.uid;
        QueryString q = new QueryString();
        q.add("uid", uid);
        q.add("value", sign);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_USER_UPDATE_SIGN), q, callback);
    }

    public static void updateBirthday(String birthday, HttpCallback callback){
        int uid = app.me.uid;
        QueryString q = new QueryString();
        q.add("uid", uid);
        q.add("value", birthday);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_USER_UPDATE_BIRTHDAY), q, callback);
    }

    public static void updateHeadImage(String headimage, HttpCallback callback){
        int uid = app.me.uid;
        QueryString q = new QueryString();
        q.add("uid", uid);
        q.add("value", headimage);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_USER_UPDATE_HEADIMAGE), q, callback);
    }

    public static void updateUserInfo(QueryString q, HttpCallback callback){
        int uid = app.me.uid;
        //QueryString q = new QueryString();

        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_USER_UPDATE_USER_INFO), q, callback);
    }

    public static void feedback(String nickname, HttpCallback callback){

    }
}
