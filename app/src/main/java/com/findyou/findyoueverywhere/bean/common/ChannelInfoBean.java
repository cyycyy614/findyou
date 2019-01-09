package com.findyou.findyoueverywhere.bean.common;

public class ChannelInfoBean {
    public enum UrlType{
        Apk,
        Web
    }
    public String name;
    public String url;
    public int urlType;
    public int status;
    public int update; //是否强制更新0:不,1:强
}
