package com.findyou.findyoueverywhere.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    //Day:日期字符串例如 2015-3-10  Num:需要减少的天数例如 7
    public static String getDateStr(String day, int Num) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //如果需要向后计算日期 -改为+
        Date newDate2 = new Date(nowDate.getTime() - (long)Num * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }

    public static String toString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newDate = simpleDateFormat.format(date);
        return newDate;
    }

    public static String addHours(Date date, int hours){
        Date d = new Date(date.getTime() + (long)hours * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newDate = simpleDateFormat.format(d);
        return newDate;
    }

    public static String addDays(Date date, int day){
        Date d = new Date(date.getTime() + (long)day * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newDate = simpleDateFormat.format(d);
        return newDate;
    }
}
