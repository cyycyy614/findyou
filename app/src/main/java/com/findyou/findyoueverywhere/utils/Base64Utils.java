package com.findyou.findyoueverywhere.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Utils {
    //加密
    public static String encode(String str){
        String encodeBase64 = "";
        try {
            encodeBase64 = Base64.encodeToString(str.getBytes("utf-8"), Base64.NO_WRAP);
            System.out.println(encodeBase64);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encodeBase64;
    }

    // 解密
    public static String getFromBase64(String base64Token) {
        String str = "";
        try {
            byte[] bytes = Base64.decode(base64Token,Base64.DEFAULT);
            str = new String(bytes,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

}
