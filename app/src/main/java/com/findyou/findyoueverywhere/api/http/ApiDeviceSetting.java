package com.findyou.findyoueverywhere.api.http;

import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.constant.ApiConst;
import com.findyou.findyoueverywhere.utils.http.HttpCallback;
import com.findyou.findyoueverywhere.utils.http.HttpClient;
import com.findyou.findyoueverywhere.utils.http.QueryString;

public class ApiDeviceSetting {

    public static void setItem(QueryString q, HttpCallback callback){
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_DEVICE_SET_ITEM), q, callback);
    }

    public static void getItem(int childid, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("childid", childid);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_DEVICE_GET_ITEM), q, callback);
    }
}
