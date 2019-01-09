package com.findyou.findyoueverywhere.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.findyou.findyoueverywhere.app.app;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by LaoZhao on 2017/11/19.
 */
public class NotificationUtils {

    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";
    private static String channelId = "通知";
    private static String channelName = "通知";
    private static int importance = NotificationManager.IMPORTANCE_DEFAULT;

    public NotificationUtils(){
    }

    private static class Singleton {
        private static final NotificationUtils INTANCE = new NotificationUtils();
    }

    public static NotificationUtils getInstance() {
        return Singleton.INTANCE;
    }

    public void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.RED); //小红点颜色
            getManager().createNotificationChannel(channel);
        }
    }

    private NotificationManager getManager(){
        if (manager == null){
            manager = (NotificationManager) app.getContext().getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification getChannelNotification(String title, String content){
        Notification.Builder builder = new Notification.Builder(app.getContext(),channelId);
        builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true);
        return builder.build();
    }

    public NotificationCompat.Builder getNotification_25(String title, String content){
        return new NotificationCompat.Builder(app.getContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }

    public void sendNotification(String title, String content){
        if (Build.VERSION.SDK_INT>=26){
            //createNotificationChannel();
            Notification notification = getChannelNotification(title, content);
            getManager().notify(1, notification);
        }else{
            Notification notification = getNotification_25(title, content).build();
            getManager().notify(1, notification);
        }
    }
}