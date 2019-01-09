package com.findyou.findyoueverywhere.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Administrator on 2018/2/3 0003.
 */

public class QQUtils {
    /**
     * 通过传入qq号获取跳转url
     *
     * @param qq
     * @return
     */
    public static String getSplitQQUri(String qq) {
        return "mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "&version=1";
    }

    /**
     * 传入qq号跳转到QQ的聊天页
     * @param context
     * @param qq
     */
    public static void go2QQChat(Context context, String qq) {
        String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "&version=1";
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
    }
}
