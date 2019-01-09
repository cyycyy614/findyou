package com.findyou.findyoueverywhere.utils.json;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/18 0018.
 * http请求后返回来的数据
 */
public class JObject implements Serializable {

    private JSONObject jsonObject;

    public JObject(String jsonData) {
        try {
            if (!TextUtils.isEmpty(jsonData)) {
                jsonObject = new JSONObject(jsonData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JObject() {
        try {
            jsonObject = new JSONObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(String name, String value) {
        try {
            jsonObject.put(name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(String name, boolean value) {
        try {
            jsonObject.put(name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(String name, double value) {
        try {
            jsonObject.put(name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(String name, int value) {
        try {
            jsonObject.put(name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(String key) {
        return null == jsonObject ? "" : jsonObject.optString(key);
    }

    public int getInt(String key) {
        return null == jsonObject ? -1 : jsonObject.optInt(key);
    }

    public double getDouble(String key) {
        return null == jsonObject ? -1 : jsonObject.optDouble(key);
    }

    public boolean getBoolean(String key) {
        return null != jsonObject && jsonObject.optBoolean(key);
    }

    public long getLong(String key) {
        return null == jsonObject ? -1 : jsonObject.optLong(key);
    }

    public String toString() { // 不可 删除
        return null == jsonObject ? "" : jsonObject.toString();
    }
    
}
