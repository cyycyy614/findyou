package com.findyou.findyoueverywhere.api.http;

import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.constant.ApiConst;
import com.findyou.findyoueverywhere.utils.http.HttpCallback;
import com.findyou.findyoueverywhere.utils.http.HttpClient;
import com.findyou.findyoueverywhere.utils.http.QueryString;

public class ApiEfence {
    public static void add(QueryString q, HttpCallback callback){
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_EFENCE_ADD_ITEM), q, callback);
    }

    public static void update(QueryString q, HttpCallback callback){
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_EFENCE_UPDATE_ITEM), q, callback);
    }

    public static void remove(int id, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("id", id);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_EFENCE_DEL_ITEM), q, callback);
    }

    public static void getItems(int id, int pageIndex, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("childid", id);
        q.add("pageIndex", pageIndex);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_EFENCE_LIST), q, callback);
    }

    public static void getItem(int id, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("id", id);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_EFENCE_GET_ITEM), q, callback);
    }
}
