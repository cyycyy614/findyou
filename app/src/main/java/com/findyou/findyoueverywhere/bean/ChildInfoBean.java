package com.findyou.findyoueverywhere.bean;

import java.util.Date;

public class ChildInfoBean {
    public int id;
    public String imei = "";
    public String headimage = "";
    public String name = "";
    public int category = -1;
    public int sex = -1;
    public String birthday = "0000-00-00";

    //选填
    public String phone = "";
    public String address = "";
    public int health = -1;
    public int looks = -1;
    public String contact = "";
    public String contact_phone = "";
    public String create_time;
    public String guardian;
    public String deviceId;
    public String psk;
    public String device_address;
    public double latitude;
    public double longitude;
    public boolean isChangeImei = false;
}
