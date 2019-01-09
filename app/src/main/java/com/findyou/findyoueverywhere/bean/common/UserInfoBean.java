package com.findyou.findyoueverywhere.bean.common;

public class UserInfoBean {
    public int uid;
    public String headimage;
    public String nickname = "";
    public String sign;
    public String phone;
    public int sex;
    public String birthday;
    public double coin = 0;
    public int userLevel = 1;
    public String token;
    public boolean isRealNameVerify; //实名认证
    public int fans;
    public int followers;
    public String location;
    public int applyLiveStatus; //申请成为主播:-1:未申请,0:未审核,1:通过,2:未通过
    public String email;
    public int child; //被监护人个数
    public int guardian;  //监护人个数
    public int main_guar;  //监护人个数
}
