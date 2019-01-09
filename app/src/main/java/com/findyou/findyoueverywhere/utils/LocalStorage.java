package com.findyou.findyoueverywhere.utils;

import android.app.Application;
import android.content.SharedPreferences;

import com.findyou.findyoueverywhere.app.app;

/**
 * Created by Administrator on 2016/8/16 0016.
 */
public class LocalStorage {
    private static final String localFileName = "xxxx_local_storage";

    public static void setItem(String key, String value){
        SharedPreferences settings = app.getContext().getSharedPreferences(localFileName, Application.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getItem(String key){
        SharedPreferences sp = app.getContext().getSharedPreferences(localFileName, Application.MODE_PRIVATE);
        String value = sp.getString(key, "");
        return value;
    }

//    public static void setIntItem(String key, int value){
//        SharedPreferences settings = MyApp.getContext().getSharedPreferences(localFileName, Application.MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putInt(key, value);
//        editor.apply();
//    }
//
//    public static int getIntItem(String key){
//        SharedPreferences sp = MyApp.getContext().getSharedPreferences(localFileName, Application.MODE_PRIVATE);
//        return sp.getInt(key, -1);
//    }
}
