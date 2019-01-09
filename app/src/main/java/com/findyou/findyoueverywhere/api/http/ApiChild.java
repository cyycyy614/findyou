package com.findyou.findyoueverywhere.api.http;

import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.bean.ChildInfoBean;
import com.findyou.findyoueverywhere.constant.ApiConst;
import com.findyou.findyoueverywhere.utils.http.HttpCallback;
import com.findyou.findyoueverywhere.utils.http.HttpClient;
import com.findyou.findyoueverywhere.utils.http.QueryString;

public class ApiChild {
    public static void add(ChildInfoBean info, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("imei", info.imei);
        q.add("headimage", info.headimage);
        q.add("name", info.name);
        q.add("category", info.category);
        q.add("sex", info.sex);
        q.add("birthday", info.birthday);
        //选填
        q.add("phone", info.phone);
        q.add("address", info.address);
        q.add("health", info.health);
        q.add("looks", info.looks);
        q.add("contact", info.contact);
        q.add("contact_phone", info.contact_phone);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_child_ADD), q, callback);
    }

    public static void update(ChildInfoBean info, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("childid", info.id);
        q.add("imei", info.imei);
        q.add("headimage", info.headimage);
        q.add("name", info.name);
        q.add("category", info.category);
        q.add("sex", info.sex);
        q.add("birthday", info.birthday);
        //选填
        q.add("phone", info.phone);
        q.add("address", info.address);
        q.add("health", info.health);
        q.add("looks", info.looks);
        q.add("contact", info.contact);
        q.add("contact_phone", info.contact_phone);
        q.add("isChangeImei", info.isChangeImei);
        q.add("deviceId", info.deviceId);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_child_UPDATE_ITEM), q, callback);
    }

    public static void getItems(int pageIndex, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("pageIndex", pageIndex);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_child_LIST), q, callback);
    }

    public static void getItem(int childid, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("childid", childid);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_child_GET_ITEM), q, callback);
    }

    public static void changeImei(int childid, String name, String imei, String deviceId, String address, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("childid", childid);
        q.add("name", name);
        q.add("imei", imei);
        q.add("deviceId", deviceId);
        q.add("device_address", address);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_child_UPDATE_IMEI), q, callback);
    }

    public static void isExistBindDevice(HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_CHILD_IS_EXIST_BIND_DEVICE), q, callback);
    }

    public static void getConstant(HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_CHILD_IS_EXIST_CONST), q, callback);
    }

    public static void unbind(int childid, String name, String deviceId, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("childid", childid);
        q.add("name", name);
        q.add("deviceId", deviceId);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_GUARDIAN_UNBIND), q, callback);
    }
}
