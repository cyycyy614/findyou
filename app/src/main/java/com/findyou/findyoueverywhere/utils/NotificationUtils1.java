//package com.findyou.findyoueverywhere.utils;
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.Build;
//import android.support.v4.app.NotificationCompat;
//import android.view.View;
//
//import com.findyou.findyoueverywhere.R;
//import com.findyou.findyoueverywhere.app.app;
//
//import java.lang.reflect.Field;
//
//import cn.jpush.android.api.CustomPushNotificationBuilder;
//import cn.jpush.android.api.JPushInterface;
//
//import static android.content.Context.NOTIFICATION_SERVICE;
//
//public class NotificationUtils1 {
//    private static String channelId = "通知";
//    private static String channelName = "通知";
//    //private static int importance = NotificationManager.IMPORTANCE_HIGH;
//    private static int importance = NotificationManager.IMPORTANCE_DEFAULT;
//
//    @TargetApi(Build.VERSION_CODES.O)
//    public static void createNotificationChannel() {
//        try{
//            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
//            NotificationManager notificati//是否在桌面icon右上角展示小红点onManager = (NotificationManager) app.getContext().getSystemService(
//                    NOTIFICATION_SERVICE);
//            channel.enableLights(true);
//            channel.setLightColor(Color.RED); //小红点颜色
//            notificationManager.createNotificationChannel(channel);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//
//    public static void sendNotification(String title, String content) {
////        NotificationManager manager = (NotificationManager) app.getContext().getSystemService(NOTIFICATION_SERVICE);
////        Notification notification = new NotificationCompat.Builder(app.getContext(), channelId)
////                .setContentTitle("收到一条聊天消息")
////                .setContentText("今天中午吃什么？")
////                .setWhen(System.currentTimeMillis())
////                .setSmallIcon(R.drawable.logo)
////                .setLargeIcon(BitmapFactory.decodeResource(app.getContext().getResources(), R.drawable.logo))
////                .setAutoCancel(true)
////                .build();
////        manager.notify(1, notification);
//
//        int notificationId = 0x1234;
//        Notification.Builder builder = new Notification.Builder(app.getContext(),channelId);
//        builder.setSmallIcon(android.R.drawable.stat_notify_chat)
//                .setContentTitle(title)
//                .setContentText(content);
//        Bitmap bitmap = BitmapFactory.decodeResource(app.getContext().getResources(),R.drawable.default_256, null);
//        builder.setLargeIcon(bitmap);
//
//        builder.setAutoCancel(true);
//        NotificationManager notificationManager = (NotificationManager) app.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(notificationId, builder.build());
//    }
//
//    public static void setStyleCustom(Activity activity) {
//        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(activity, R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
//        builder.layoutIconDrawable = R.drawable.logo;
//        builder.developerArg0 = "developerArg2";
//        JPushInterface.setPushNotificationBuilder(2, builder);
//        //Toast.makeText(PushSetActivity.this, "Custom Builder - 2", Toast.LENGTH_SHORT).show();
//        //ToastUtils.showToast(activity, );
//    }
//}
