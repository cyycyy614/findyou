package com.findyou.findyoueverywhere.utils.http;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class QueryString {
    private JSONObject json = new JSONObject();
    public JSONObject add(String name, Object value){
        try{
            json.put(name, value);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean getBoolean(String name){
        boolean ret = false;
        try {
            if(json.isNull(name))
                return ret;
            ret = json.getBoolean(name);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }

    public String toString(){
        return json.toString();
    }
}
