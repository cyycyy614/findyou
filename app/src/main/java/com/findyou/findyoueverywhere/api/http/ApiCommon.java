package com.findyou.findyoueverywhere.api.http;

import com.blankj.utilcode.util.AppUtils;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.constant.ApiConst;
import com.findyou.findyoueverywhere.utils.ChannelUtils;
import com.findyou.findyoueverywhere.utils.PermissionsUtils;
import com.findyou.findyoueverywhere.utils.http.HttpCallback;
import com.findyou.findyoueverywhere.utils.http.HttpClient;
import com.findyou.findyoueverywhere.utils.http.QueryString;

public class ApiCommon {
    public static void help(int pid, int pageIndex, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("pid", pid);
        q.add("pageIndex", pageIndex);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_COMMON_HELP), q, callback);
    }

    public static void getHelpItem(int id, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("id", id);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_COMMON_HELP_ITEM), q, callback);
    }

    public static void getAppInfo(HttpCallback callback){
        String packageName = AppUtils.getAppPackageName();
        String channel = ChannelUtils.getChannelName();
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("packageName", packageName);
        q.add("channel", channel);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_COMMON_APP_INFO), q, callback);
    }

    public static void getMsgItems(int type, int pageIndex, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("type", type);
        q.add("pageIndex", pageIndex);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_COMMON_GET_MSG), q, callback);
    }

    public static void searchHelpItems(String keyword, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("keyword", keyword);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_COMMON_HELP_SEARCH), q, callback);
    }

    public static void getBanners(int type, int count, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("type", type);
        q.add("count", count);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_COMMON_GET_BANNERS), q, callback);
    }

    public static void getNewsItems(int type, int pageIndex, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("type", type);
        q.add("pageIndex", pageIndex);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_COMMON_NEWS_LIST), q, callback);
    }

    public static void getNewsItem(int id, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("id", id);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_COMMON_NEWS_ITEM), q, callback);
    }

    public static void addStoryItem(int type, String content, String images, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("type", type);
        q.add("content", content);
        q.add("images", images);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_COMMON_ADD_STORY_ITEM), q, callback);
    }

    public static void getStoryItems(int type, int pageIndex, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("type", type);
        q.add("pageIndex", pageIndex);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_COMMON_GET_STORY_ITEMS), q, callback);
    }

    public static void likeStoryItems(int id, String like, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("id", id);
        q.add("like", like);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_COMMON_LIKE_STORY_ITEM), q, callback);
    }

    public static void commentStoryItems(int id, String comment, HttpCallback callback){
        QueryString q = new QueryString();
        q.add("uid", app.me.uid);
        q.add("id", id);
        q.add("comment", comment);
        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_COMMON_COMMENT_STORY_ITEM), q, callback);
    }

//    public static void getStoryCommentItems(int id, HttpCallback callback){
//        QueryString q = new QueryString();
//        q.add("uid", app.me.uid);
//        q.add("id", id);
//        HttpClient.doPost(ApiConst.getUrlNew(ApiConst.API_COMMON_COMMENT_STORY_ITEMS), q, callback);
//    }
}
