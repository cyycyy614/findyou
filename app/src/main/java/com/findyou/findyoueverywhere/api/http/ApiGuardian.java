package com.findyou.findyoueverywhere.api.http;

import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.bean.ChildInfoBean;
import com.findyou.findyoueverywhere.constant.ApiConst;
import com.findyou.findyoueverywhere.utils.http.HttpCallback;
import com.findyou.findyoueverywhere.utils.http.HttpClient;
import com.findyou.findyoueverywhere.utils.http.QueryString;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ApiGuardian {
    public static void add(int childid, String name, String phone, int status, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("childid", childid);
        q.add("name", name);
        q.add("phone", phone);
        q.add("status", status);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_ADD), q, callback);
    }

    public static void update(int id, String name, String phone, int status, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("id", id);
        q.add("name", name);
        q.add("phone", phone);
        q.add("status", status);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_UPDATE), q, callback);
    }

    public static void getItems(int childid, int pageIndex, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("childid", childid);
        q.add("pageIndex", pageIndex);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_LIST), q, callback);
    }

    public static void setMain(int id, int childid, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("id", id);
        q.add("childid", childid);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_SET_MAIN), q, callback);
    }
}
