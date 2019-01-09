package com.findyou.findyoueverywhere.utils;

import android.text.TextUtils;

public class ChannelUtils {
    public static String getChannelName(){
        //String channel = PackerNg.getChannel(MyApp.getContext());
        String channel = "";
        if(TextUtils.isEmpty(channel)){
            channel = "official";
        }
        return channel;
    }
}
