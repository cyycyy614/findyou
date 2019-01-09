package com.findyou.findyoueverywhere.api.http;

import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.constant.ApiConst;
import com.findyou.findyoueverywhere.utils.http.HttpCallback;
import com.findyou.findyoueverywhere.utils.http.HttpClient;
import com.findyou.findyoueverywhere.utils.http.QueryString;

public class ApiWarning {
    public static void setting(QueryString q, HttpCallback callback){
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_WARN_SET), q, callback);
    }

    public static void getItem(int id, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("childid", id);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_WARN_GET), q, callback);
    }

    public static void getItems(int id, String imei, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("childid", id);
        //q.add("imei", imei);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_WARN_HISTORY), q, callback);
    }
}
