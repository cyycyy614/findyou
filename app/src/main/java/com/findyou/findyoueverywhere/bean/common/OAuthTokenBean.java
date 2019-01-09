package com.findyou.findyoueverywhere.bean.common;

public class OAuthTokenBean {
    public String access_token; //token
    public String token_type; //类型
    public int expires_in; //token的超时时间
    public String refresh_token; //刷新token
}
